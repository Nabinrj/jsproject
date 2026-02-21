package com.hallsymphony.model;

public enum HallType {
    AUDITORIUM(1000, 300.0),
    BANQUET_HALL(300, 100.0),
    MEETING_ROOM(30, 50.0);

    private final int capacity;
    private final double hourlyRate;

    HallType(int capacity, double hourlyRate) {
        this.capacity = capacity;
        this.hourlyRate = hourlyRate;
    }

    public int getCapacity() { return capacity; }
    public double getHourlyRate() { return hourlyRate; }
}
