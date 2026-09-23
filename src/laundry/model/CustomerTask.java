package laundry.model;

import java.util.concurrent.ThreadLocalRandom;

/** Simulates one customer journey, using interruptible resource waits and guaranteed releases. */
public final class CustomerTask implements Runnable {
    private final int customerId; private final LaundryFacility facility; private final Statistics statistics; private final EventSink events;
    public interface EventSink { void event(int customerId, String message); }
    public CustomerTask(int customerId, LaundryFacility facility, Statistics statistics, EventSink events) {
        this.customerId = customerId; this.facility = facility; this.statistics = statistics; this.events = events;
    }
    @Override public void run() {
        long started = System.currentTimeMillis();
        try { wash(); dry(); pay(); statistics.customerServed(System.currentTimeMillis() - started); say("left the facility"); }
        catch (InterruptedException interrupted) { Thread.currentThread().interrupt(); say("left because the simulation stopped"); }
    }
    private void wash() throws InterruptedException {
        while (true) {
            say("waiting for a washer"); facility.washers().acquire(); int slot = facility.claimWasher(); statistics.washerStarted();
            try { say("washing in washer " + (slot + 1)); pause(4, 6); if (chance(5)) { say("washer failed; retrying"); continue; } return; }
            finally { statistics.washerFinished(); facility.releaseWasher(slot); facility.washers().release(); }
        }
    }
    private void dry() throws InterruptedException {
        say("waiting for a dryer"); facility.dryers().acquire(); int slot = facility.claimDryer(); statistics.dryerStarted();
        try { say("drying in dryer " + (slot + 1)); pause(3, 5); }
        finally { statistics.dryerFinished(); facility.releaseDryer(slot); facility.dryers().release(); }
    }
    private void pay() throws InterruptedException {
        say("joining payment queue"); facility.enterPaymentQueue();
        try {
            while (facility.kiosksBrokenForDay()) { say("waiting: kiosks are broken for the day"); Thread.sleep(1_000); }
            boolean paid = false;
            while (!paid) {
                facility.kiosks().acquire(); int slot = facility.claimKiosk();
                try { say("paying at kiosk " + (slot + 1)); pause(1, 2); paid = !chance(5); if (!paid) say("kiosk failed; retrying after 2 seconds"); }
                finally { facility.releaseKiosk(slot); facility.kiosks().release(); }
                if (!paid) Thread.sleep(2_000);
            }
        } finally { facility.leavePaymentQueue(); }
    }
    private void pause(int minimumSeconds, int maximumSeconds) throws InterruptedException { Thread.sleep(ThreadLocalRandom.current().nextInt(minimumSeconds, maximumSeconds + 1) * 1_000L); }
    private boolean chance(int percent) { return ThreadLocalRandom.current().nextInt(100) < percent; }
    private void say(String message) { events.event(customerId, "[" + Thread.currentThread().getName() + "] Customer " + customerId + " " + message); }
}
