package com.hallsymphony.model;

public class Hall {
    private final String id;
    private String name;
    private HallType type;
    private String availabilityRemark;

    public Hall(String id, String name, HallType type, String availabilityRemark) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.availabilityRemark = availabilityRemark;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public HallType getType() { return type; }
    public String getAvailabilityRemark() { return availabilityRemark; }
    public void setName(String name) { this.name = name; }
    public void setType(HallType type) { this.type = type; }
    public void setAvailabilityRemark(String availabilityRemark) { this.availabilityRemark = availabilityRemark; }

    public String toRecord() {
        return String.join("|", id, name, type.name(), availabilityRemark == null ? "" : availabilityRemark);
    }

    public static Hall fromRecord(String row) {
        String[] p = row.split("\\|", -1);
        return new Hall(p[0], p[1], HallType.valueOf(p[2]), p.length > 3 ? p[3] : "");
    }
}
