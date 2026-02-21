package com.hallsymphony.model;

public class Administrator extends User {
    public Administrator(String id, String username, String password, String fullName, boolean blocked) {
        super(id, username, password, fullName, Role.ADMINISTRATOR, blocked);
    }
}
