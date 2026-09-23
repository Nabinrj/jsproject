package laundry.model;

import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/** Owns the shared machines and safely publishes their state to the user interface. */
public final class LaundryFacility {
    public static final int WASHERS = 6, DRYERS = 4, KIOSKS = 2, OWNER_CALL_THRESHOLD = 30;
    private final Semaphore washerPermits = new Semaphore(WASHERS, true);
    private final Semaphore dryerPermits = new Semaphore(DRYERS, true);
    private final Semaphore kioskPermits = new Semaphore(KIOSKS, true);
    // A semaphore limits quantity; this lock makes the scan-and-claim of a particular GUI slot atomic.
    private final ReentrantLock slotLock = new ReentrantLock();
    private final boolean[] washerBusy = new boolean[WASHERS], dryerBusy = new boolean[DRYERS], kioskBusy = new boolean[KIOSKS];
    private final AtomicInteger paymentQueue = new AtomicInteger();
    // Atomic compare-and-set ensures only one customer triggers the owner notification.
    private final AtomicBoolean ownerCalled = new AtomicBoolean();
    // Volatile is sufficient for this independently read/written visibility flag.
    private volatile boolean kiosksBrokenForDay;
    private volatile Listener listener = Listener.NONE;

    public interface Listener {
        Listener NONE = new Listener() { public void machinesChanged() { } public void ownerCalled() { } };
        void machinesChanged(); void ownerCalled();
    }
    public void setListener(Listener listener) { this.listener = listener == null ? Listener.NONE : listener; }
    public Semaphore washers() { return washerPermits; }
    public Semaphore dryers() { return dryerPermits; }
    public Semaphore kiosks() { return kioskPermits; }
    public boolean kiosksBrokenForDay() { return kiosksBrokenForDay; }

    public void setKiosksBrokenForDay(boolean value) { kiosksBrokenForDay = value; }
    public int enterPaymentQueue() {
        int queued = paymentQueue.incrementAndGet();
        if (queued >= OWNER_CALL_THRESHOLD && ownerCalled.compareAndSet(false, true)) listener.ownerCalled();
        return queued;
    }
    public void leavePaymentQueue() { paymentQueue.decrementAndGet(); }
    public int paymentQueueSize() { return paymentQueue.get(); }

    public int claimWasher() { return claimSlot(washerBusy); }
    public int claimDryer() { return claimSlot(dryerBusy); }
    public int claimKiosk() { return claimSlot(kioskBusy); }
    public void releaseWasher(int slot) { releaseSlot(washerBusy, slot); }
    public void releaseDryer(int slot) { releaseSlot(dryerBusy, slot); }
    public void releaseKiosk(int slot) { releaseSlot(kioskBusy, slot); }
    public boolean[] washerState() { return snapshot(washerBusy); }
    public boolean[] dryerState() { return snapshot(dryerBusy); }
    public boolean[] kioskState() { return snapshot(kioskBusy); }

    private int claimSlot(boolean[] slots) {
        slotLock.lock();
        try { for (int i = 0; i < slots.length; i++) if (!slots[i]) { slots[i] = true; listener.machinesChanged(); return i; } }
        finally { slotLock.unlock(); }
        throw new IllegalStateException("A permit was acquired without a free machine slot");
    }
    private void releaseSlot(boolean[] slots, int slot) {
        slotLock.lock();
        try { slots[slot] = false; listener.machinesChanged(); }
        finally { slotLock.unlock(); }
    }
    private boolean[] snapshot(boolean[] slots) { slotLock.lock(); try { return Arrays.copyOf(slots, slots.length); } finally { slotLock.unlock(); } }
}
