package com.hallsymphony.model;

public class Customer extends User {
    public Customer(String id, String username, String password, String fullName, boolean blocked) {
        super(id, username, password, fullName, Role.CUSTOMER, blocked);
    }
}
