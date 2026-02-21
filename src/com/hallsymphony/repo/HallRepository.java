package com.hallsymphony.repo;

import com.hallsymphony.model.Hall;
import com.hallsymphony.util.TextFileStore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HallRepository {
    private final TextFileStore store = new TextFileStore("data/halls.txt");

    public List<Hall> findAll() {
        List<Hall> halls = new ArrayList<>();
        for (String row : store.readAll()) if (!row.isBlank()) halls.add(Hall.fromRecord(row));
        return halls;
    }

    public Hall save(Hall hall) {
        List<Hall> halls = findAll();
        halls.removeIf(h -> h.getId().equals(hall.getId()));
        halls.add(hall);
        store.writeAll(halls.stream().map(Hall::toRecord).toList());
        return hall;
    }

    public void delete(String hallId) {
        List<Hall> halls = findAll();
        halls.removeIf(h -> h.getId().equals(hallId));
        store.writeAll(halls.stream().map(Hall::toRecord).toList());
    }

    public String nextId() { return "H-" + UUID.randomUUID().toString().substring(0, 8); }
}
