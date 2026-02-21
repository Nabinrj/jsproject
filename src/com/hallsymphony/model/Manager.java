package com.hallsymphony.model;

public class Manager extends User {
    public Manager(String id, String username, String password, String fullName, boolean blocked) {
        super(id, username, password, fullName, Role.MANAGER, blocked);
    }
}
