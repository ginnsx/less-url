package com.github.xioshe.less.url.exception;

import lombok.Getter;

@Getter
public class DistributedLockException extends RuntimeException {

    public enum LockErrorType {
        LOCK_ACQUISITION_TIMEOUT,
        LOCK_ALREADY_HELD,
        LOCK_RELEASE_FAILED,
        UNEXPECTED_ERROR
    }

    private final LockErrorType errorType;
    private final String lockKey;

    public DistributedLockException(LockErrorType errorType, String lockKey) {
        this(errorType, lockKey, null);
    }

    public DistributedLockException(LockErrorType errorType, String lockKey, String message) {
        super(formatMessage(errorType, lockKey, message));
        this.errorType = errorType;
        this.lockKey = lockKey;
    }

    public DistributedLockException(LockErrorType errorType, String lockKey, String message, Throwable cause) {
        super(formatMessage(errorType, lockKey, message), cause);
        this.errorType = errorType;
        this.lockKey = lockKey;
    }

    private static String formatMessage(LockErrorType errorType, String lockKey, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Distributed lock error: ").append(errorType);
        sb.append(" for lock key: ").append(lockKey);
        if (message != null && !message.isEmpty()) {
            sb.append(". ").append(message);
        }
        return sb.toString();
    }
}
