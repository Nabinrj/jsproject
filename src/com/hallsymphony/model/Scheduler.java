package com.hallsymphony.model;

public class Scheduler extends User {
    public Scheduler(String id, String username, String password, String fullName, boolean blocked) {
        super(id, username, password, fullName, Role.SCHEDULER, blocked);
    }
}
