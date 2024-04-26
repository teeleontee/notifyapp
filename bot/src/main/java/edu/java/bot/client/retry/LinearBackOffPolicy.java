package edu.java.bot.client.retry;

import java.util.function.Supplier;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.retry.backoff.SleepingBackOffPolicy;
import org.springframework.retry.backoff.ThreadWaitSleeper;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class LinearBackOffPolicy implements SleepingBackOffPolicy<LinearBackOffPolicy> {

    protected final Log logger = LogFactory.getLog(this.getClass());

    /**
     * The default 'initialInterval' value - 100 millisecs. Coupled with the default
     * 'multiplier' value this gives a useful initial spread of pauses for 1-5 retries.
     */
    public static final long DEFAULT_INITIAL_INTERVAL = 100L;

    /**
     * The default maximum backoff time (30 seconds).
     */
    public static final long DEFAULT_MAX_INTERVAL = 30000L;

    /**
     * The default 'increment' value - value 100.
     */
    public static final double DEFAULT_INCREMENT = DEFAULT_INITIAL_INTERVAL;

    /**
     * The initial backoff interval.
     */
    private long initialInterval = DEFAULT_INITIAL_INTERVAL;

    /**
     * The maximum value of the backoff period in milliseconds.
     */
    private long maxInterval = DEFAULT_MAX_INTERVAL;

    /**
     * The value to add to the backoff period for each retry attempt.
     */
    private double incrementer = DEFAULT_INCREMENT;

    /**
     * The initial backoff interval.
     */
    private Supplier<Long> initialIntervalSupplier;

    /**
     * The maximum value of the backoff period in milliseconds.
     */
    private Supplier<Long> maxIntervalSupplier;

    /**
     * The value to add to the backoff period for each retry attempt.
     */
    private Supplier<Double> incrementerSupplier;

    /**
     * Public setter for the
     * strategy.
     */
    @Setter
    private Sleeper sleeper = new ThreadWaitSleeper();

    @Override
    public LinearBackOffPolicy withSleeper(Sleeper sleeper) {
        LinearBackOffPolicy res = newInstance();
        cloneValues(res);
        res.setSleeper(sleeper);
        return res;
    }

    protected LinearBackOffPolicy newInstance() {
        return new LinearBackOffPolicy();
    }

    protected void cloneValues(LinearBackOffPolicy target) {
        target.setInitialInterval(getInitialInterval());
        target.setMaxInterval(getMaxInterval());
        target.setIncrementer(getIncrementer());
        target.setSleeper(this.sleeper);
    }

    /**
     * Set the initial sleep interval value. Default is {@code 100} millisecond. Cannot be
     * set to a value less than one.
     *
     * @param initialInterval the initial interval
     */
    public void setInitialInterval(long initialInterval) {
        this.initialInterval = initialInterval > 1 ? initialInterval : 1;
    }

    /**
     * Set the multiplier value. Default is '<code>2.0</code>'. Hint: do not use values
     * much in excess of 1.0 (or the backoff will get very long very fast).
     *
     * @param incrementer the multiplier
     */
    public void setIncrementer(double incrementer) {
        this.incrementer = Math.max(incrementer, 1.0);
    }

    /**
     * Setter for maximum back off period. Default is 30000 (30 seconds). the value will
     * be reset to 1 if this method is called with a value less than 1. Set this to avoid
     * infinite waits if backing off a large number of times (or if the multiplier is set
     * too high).
     *
     * @param maxInterval in milliseconds.
     */
    public void setMaxInterval(long maxInterval) {
        this.maxInterval = maxInterval > 0 ? maxInterval : 1;
    }

    /**
     * Set the initial sleep interval value. Default supplier supplies {@code 100}
     * millisecond.
     *
     * @param initialIntervalSupplier the initial interval
     * @since 2.0
     */
    public void initialIntervalSupplier(Supplier<Long> initialIntervalSupplier) {
        Assert.notNull(initialIntervalSupplier, "'initialIntervalSupplier' cannot be null");
        this.initialIntervalSupplier = initialIntervalSupplier;
    }

    /**
     * Set the incrementer value.
     *
     * @param incrementerSupplier the multiplier
     * @since 2.0
     */
    public void incrementerSupplier(Supplier<Double> incrementerSupplier) {
        Assert.notNull(incrementerSupplier, "'incrementerSupplier' cannot be null");
        this.incrementerSupplier = incrementerSupplier;
    }

    /**
     * Setter for maximum back off period. Default is 30000 (30 seconds). the value will
     * be reset to 1 if this method is called with a value less than 1. Set this to avoid
     * infinite waits if backing off a large number of times (or if the multiplier is set
     * too high).
     *
     * @param maxIntervalSupplier in milliseconds.
     * @since 2.0
     */
    public void maxIntervalSupplier(Supplier<Long> maxIntervalSupplier) {
        Assert.notNull(maxIntervalSupplier, "'maxIntervalSupplier' cannot be null");
        this.maxIntervalSupplier = maxIntervalSupplier;
    }

    protected Supplier<Long> getInitialIntervalSupplier() {
        return initialIntervalSupplier;
    }

    protected Supplier<Long> getMaxIntervalSupplier() {
        return maxIntervalSupplier;
    }

    protected Supplier<Double> getIncrementerSupplier() {
        return incrementerSupplier;
    }

    /**
     * The initial period to sleep on the first backoff.
     *
     * @return the initial interval
     */
    public long getInitialInterval() {
        return this.initialIntervalSupplier != null ? this.initialIntervalSupplier.get() : this.initialInterval;
    }

    /**
     * The maximum interval to sleep for. Defaults to 30 seconds.
     *
     * @return the maximum interval.
     */
    public long getMaxInterval() {
        return this.maxIntervalSupplier != null ? this.maxIntervalSupplier.get() : this.maxInterval;
    }

    /**
     * The incrementer to use to generate the next backoff interval from the last.
     *
     * @return the multiplier in use
     */
    public double getIncrementer() {
        return this.incrementerSupplier != null ? this.incrementerSupplier.get() : this.incrementer;
    }

    /**
     * Returns a new instance of {@link BackOffContext} with the configured properties.
     */
    @Override
    public BackOffContext start(RetryContext context) {
        return new LinearBackOffContext(this.initialInterval, this.incrementer, this.maxInterval,
            this.initialIntervalSupplier, this.incrementerSupplier, this.maxIntervalSupplier
        );
    }

    /**
     * Pause for the current backoff interval.
     */
    @Override
    public void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
        LinearBackOffPolicy.LinearBackOffContext
            context = (LinearBackOffPolicy.LinearBackOffContext) backOffContext;
        try {
            long sleepTime = context.getSleepAndIncrement();
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Sleeping for " + sleepTime);
            }
            this.sleeper.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BackOffInterruptedException("Thread interrupted while sleeping", e);
        }
    }

    @Override
    public String toString() {
        return ClassUtils.getShortName(getClass()) + "[initialInterval=" + getInitialInterval() + ", multiplier="
            + getIncrementer() + ", maxInterval=" + getMaxInterval() + "]";
    }

    static class LinearBackOffContext implements BackOffContext {

        private final double incrementer;

        private long interval;

        private final long maxInterval;

        private Supplier<Long> initialIntervalSupplier;

        private final Supplier<Double> incrementerSupplier;

        private final Supplier<Long> maxIntervalSupplier;

        LinearBackOffContext(
            long interval, double incrementer, long maxInterval,
            Supplier<Long> intervalSupplier, Supplier<Double> multiplierSupplier,
            Supplier<Long> maxIntervalSupplier
        ) {
            this.interval = interval;
            this.incrementer = incrementer;
            this.maxInterval = maxInterval;
            this.initialIntervalSupplier = intervalSupplier;
            this.incrementerSupplier = multiplierSupplier;
            this.maxIntervalSupplier = maxIntervalSupplier;
        }

        public synchronized long getSleepAndIncrement() {
            long sleep = getInterval();
            long max = getMaxInterval();
            if (sleep > max) {
                sleep = max;
            } else {
                this.interval = getNextInterval();
            }
            return sleep;
        }

        protected long getNextInterval() {
            return (long) (this.interval + getIncrementer());
        }

        public double getIncrementer() {
            return this.incrementerSupplier != null ? this.incrementerSupplier.get() : this.incrementer;
        }

        public long getInterval() {
            if (this.initialIntervalSupplier != null) {
                this.interval = this.initialIntervalSupplier.get();
                this.initialIntervalSupplier = null;
            }
            return this.interval;
        }

        public long getMaxInterval() {
            return this.maxIntervalSupplier != null ? this.maxIntervalSupplier.get() : this.maxInterval;
        }

    }

}
