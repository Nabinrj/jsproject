package com.hallsymphony.service;

import com.hallsymphony.model.Customer;
import com.hallsymphony.model.User;
import com.hallsymphony.repo.UserRepository;

import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && !user.get().isBlocked() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    public Customer registerCustomer(String username, String password, String fullName) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }
        Customer c = new Customer(userRepository.nextId(), username, password, fullName, false);
        userRepository.save(c);
        return c;
    }
}
