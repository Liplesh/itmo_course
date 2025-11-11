package ru.lipnin.itmohomework.feign.dwh;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DwhResponseRetryer implements Retryer {
    private final int maxAttempts;
    private final long backoff;
    private int attempt;
    private final long maxPeriod;

    public DwhResponseRetryer() {
        this(1000, 5000, 3);
    }

    public DwhResponseRetryer(long backoff, long maxPeriod, int maxAttempts) {
        this.backoff = backoff;
        this.maxPeriod = maxPeriod;
        this.maxAttempts = maxAttempts;
        this.attempt = 1;
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        if (attempt++ >= maxAttempts) {
            log.info("Retrying after {} attempts", attempt);
            throw e;
        }

        try {
            long sleepTime = Math.min(backoff * (attempt - 1), maxPeriod);
            log.info("Retry attempt {}/{} after {} ms. Reason: {}",
                    attempt, maxAttempts, sleepTime, e.getMessage());
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    @Override
    public Retryer clone() {
        return new DwhResponseRetryer(backoff, maxPeriod, maxAttempts);
    }
}
