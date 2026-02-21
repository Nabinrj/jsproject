package com.hallsymphony.repo;

import com.hallsymphony.model.Booking;
import com.hallsymphony.util.TextFileStore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingRepository {
    private final TextFileStore store = new TextFileStore("data/bookings.txt");

    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        for (String row : store.readAll()) if (!row.isBlank()) bookings.add(Booking.fromRecord(row));
        return bookings;
    }

    public Booking save(Booking booking) {
        List<Booking> bookings = findAll();
        bookings.removeIf(b -> b.getId().equals(booking.getId()));
        bookings.add(booking);
        store.writeAll(bookings.stream().map(Booking::toRecord).toList());
        return booking;
    }

    public String nextId() { return "B-" + UUID.randomUUID().toString().substring(0, 8); }
}
