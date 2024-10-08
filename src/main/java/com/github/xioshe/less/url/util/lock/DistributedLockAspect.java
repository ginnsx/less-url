package com.github.xioshe.less.url.util.lock;

import com.github.xioshe.less.url.exceptions.DistributedLockException;
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

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String lockKey = getLockKey(joinPoint, distributedLock);
        RLock lock = redissonClient.getLock(lockKey);

        boolean locked = false;
        try {
            locked = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.unit());
            if (!locked) {
                throw new DistributedLockException(
                        DistributedLockException.LockErrorType.LOCK_ACQUISITION_TIMEOUT,
                        distributedLock.key(),
                        "Failed to acquire lock within the specified wait time"
                );
            }
            return joinPoint.proceed();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DistributedLockException(
                    DistributedLockException.LockErrorType.UNEXPECTED_ERROR,
                    distributedLock.key(),
                    "Lock acquisition was interrupted",
                    e
            );
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    private String getLockKey(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
        String spel = distributedLock.spel();
        if (spel.isEmpty()) {
            return distributedLock.key();
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
        return distributedLock.key() + ":" + spelValue;
    }
}