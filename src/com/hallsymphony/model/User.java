package com.hallsymphony.model;

public abstract class User {
    private final String id;
    private final String username;
    private String password;
    private String fullName;
    private final Role role;
    private boolean blocked;

    protected User(String id, String username, String password, String fullName, Role role, boolean blocked) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.blocked = blocked;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public Role getRole() { return role; }
    public boolean isBlocked() { return blocked; }

    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }

    public String toRecord() {
        return String.join("|", id, username, password, fullName, role.name(), String.valueOf(blocked));
    }

    public static User fromRecord(String row) {
        String[] p = row.split("\\|", -1);
        Role role = Role.valueOf(p[4]);
        return switch (role) {
            case CUSTOMER -> new Customer(p[0], p[1], p[2], p[3], Boolean.parseBoolean(p[5]));
            case SCHEDULER -> new Scheduler(p[0], p[1], p[2], p[3], Boolean.parseBoolean(p[5]));
            case ADMINISTRATOR -> new Administrator(p[0], p[1], p[2], p[3], Boolean.parseBoolean(p[5]));
            case MANAGER -> new Manager(p[0], p[1], p[2], p[3], Boolean.parseBoolean(p[5]));
        };
    }
}
