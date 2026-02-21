package com.hallsymphony.model;

public class Issue {
    private final String id;
    private final String bookingId;
    private final String customerId;
    private String description;
    private IssueStatus status;
    private String assignedSchedulerId;

    public Issue(String id, String bookingId, String customerId, String description, IssueStatus status, String assignedSchedulerId) {
        this.id = id;
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.description = description;
        this.status = status;
        this.assignedSchedulerId = assignedSchedulerId;
    }

    public String getId() { return id; }
    public String getBookingId() { return bookingId; }
    public String getCustomerId() { return customerId; }
    public String getDescription() { return description; }
    public IssueStatus getStatus() { return status; }
    public String getAssignedSchedulerId() { return assignedSchedulerId; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(IssueStatus status) { this.status = status; }
    public void setAssignedSchedulerId(String assignedSchedulerId) { this.assignedSchedulerId = assignedSchedulerId; }

    public String toRecord() {
        return String.join("|", id, bookingId, customerId, description, status.name(), assignedSchedulerId == null ? "" : assignedSchedulerId);
    }

    public static Issue fromRecord(String row) {
        String[] p = row.split("\\|", -1);
        return new Issue(p[0], p[1], p[2], p[3], IssueStatus.valueOf(p[4]), p.length > 5 ? p[5] : "");
    }
}
