package com.hallsymphony.repo;

import com.hallsymphony.model.*;
import com.hallsymphony.util.TextFileStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserRepository {
    private final TextFileStore store = new TextFileStore("data/users.txt");

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        for (String row : store.readAll()) {
            if (!row.isBlank()) users.add(User.fromRecord(row));
        }
        return users;
    }

    public Optional<User> findByUsername(String username) {
        return findAll().stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public List<User> findByRole(Role role) {
        return findAll().stream().filter(u -> u.getRole() == role).collect(Collectors.toList());
    }

    public User save(User user) {
        List<User> users = findAll();
        users.removeIf(u -> u.getId().equals(user.getId()));
        users.add(user);
        store.writeAll(users.stream().map(User::toRecord).toList());
        return user;
    }

    public String nextId() { return "U-" + UUID.randomUUID().toString().substring(0, 8); }

    public void bootstrapDefaults() {
        if (!findAll().isEmpty()) return;
        save(new Scheduler(nextId(), "scheduler", "pass123", "Default Scheduler", false));
        save(new Administrator(nextId(), "admin", "pass123", "Default Admin", false));
        save(new Manager(nextId(), "manager", "pass123", "Default Manager", false));
    }
}
