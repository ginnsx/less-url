package com.github.xioshe.less.url.util.lock;

import com.github.xioshe.less.url.exception.DistributedLockException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(annotation)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock annotation) throws Throwable {
        String lockKey = getLockKey(joinPoint, annotation);
        RLock lock = redissonClient.getLock(lockKey);

        boolean locked = false;
        try {
            locked = lock.tryLock(annotation.waitTime(), annotation.leaseTime(), annotation.timeunit());
            if (!locked) {
                throw new DistributedLockException(
                        DistributedLockException.LockErrorType.LOCK_ACQUISITION_TIMEOUT,
                        annotation.key(),
                        "Failed to acquire lock within the specified wait time"
                );
            }
            return joinPoint.proceed();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DistributedLockException(
                    DistributedLockException.LockErrorType.UNEXPECTED_ERROR,
                    annotation.key(),
                    "Lock acquisition was interrupted",
                    e
            );
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    private String getLockKey(ProceedingJoinPoint joinPoint, DistributedLock annotation) {
        String prefix = annotation.prefix();
        String spel = annotation.spel();
        if (spel.isEmpty()) {
            return prefix + annotation.key();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] arguments = joinPoint.getArgs();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < arguments.length; i++) {
            context.setVariable(parameterNames[i], arguments[i]);
        }

        Expression expression = parser.parseExpression(spel);
        String spelValue = expression.getValue(context, String.class);
        return prefix + annotation.key() + spelValue;
    }
}