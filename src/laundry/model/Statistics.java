package laundry.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/** Thread-safe measurements collected while customer tasks run concurrently. */
public final class Statistics {
    private final AtomicInteger served = new AtomicInteger();
    private final AtomicLong totalCustomerMillis = new AtomicLong();
    private final AtomicInteger washersInUse = new AtomicInteger();
    private final AtomicInteger dryersInUse = new AtomicInteger();
    private final AtomicInteger maxWashersInUse = new AtomicInteger();
    private final AtomicInteger maxDryersInUse = new AtomicInteger();

    public void washerStarted() { updateMaximum(maxWashersInUse, washersInUse.incrementAndGet()); }
    public void washerFinished() { washersInUse.decrementAndGet(); }
    public void dryerStarted() { updateMaximum(maxDryersInUse, dryersInUse.incrementAndGet()); }
    public void dryerFinished() { dryersInUse.decrementAndGet(); }

    public void customerServed(long elapsedMillis) {
        totalCustomerMillis.addAndGet(elapsedMillis);
        served.incrementAndGet();
    }

    /** Uses compare-and-set so a stale writer cannot lower a recorded maximum. */
    private void updateMaximum(AtomicInteger maximum, int candidate) {
        int observed;
        while (candidate > (observed = maximum.get()) && !maximum.compareAndSet(observed, candidate)) {
            // Retry if another worker changed the maximum between get and compare-and-set.
        }
    }

    public Snapshot snapshot() {
        int completed = served.get();
        long average = completed == 0 ? 0 : totalCustomerMillis.get() / completed;
        return new Snapshot(completed, average, maxWashersInUse.get(), maxDryersInUse.get());
    }

    /** Immutable view that can safely be passed from worker threads to Swing. */
    public record Snapshot(int served, long averageMillis, int maxWashers, int maxDryers) { }
}
