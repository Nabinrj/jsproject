package com.hallsymphony.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Booking {
    private final String id;
    private final String customerId;
    private final String hallId;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private boolean cancelled;

    public Booking(String id, String customerId, String hallId, LocalDateTime start, LocalDateTime end, boolean cancelled) {
        this.id = id;
        this.customerId = customerId;
        this.hallId = hallId;
        this.start = start;
        this.end = end;
        this.cancelled = cancelled;
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getHallId() { return hallId; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    public long getHours() {
        return Math.max(1, Duration.between(start, end).toHours());
    }

    public String toRecord() {
        return String.join("|", id, customerId, hallId, start.toString(), end.toString(), String.valueOf(cancelled));
    }

    public static Booking fromRecord(String row) {
        String[] p = row.split("\\|", -1);
        return new Booking(p[0], p[1], p[2], LocalDateTime.parse(p[3]), LocalDateTime.parse(p[4]), Boolean.parseBoolean(p[5]));
    }
}
