package com.hallsymphony.service;

import com.hallsymphony.model.Booking;
import com.hallsymphony.model.Hall;
import com.hallsymphony.repo.BookingRepository;
import com.hallsymphony.repo.HallRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesService {
    private final BookingRepository bookingRepository;
    private final HallRepository hallRepository;

    public SalesService(BookingRepository bookingRepository, HallRepository hallRepository) {
        this.bookingRepository = bookingRepository;
        this.hallRepository = hallRepository;
    }

    public double totalSales() {
        List<Booking> bookings = bookingRepository.findAll().stream().filter(b -> !b.isCancelled()).toList();
        Map<String, Hall> hallMap = new HashMap<>();
        for (Hall h : hallRepository.findAll()) hallMap.put(h.getId(), h);

        double total = 0;
        for (Booking b : bookings) {
            Hall hall = hallMap.get(b.getHallId());
            if (hall != null) total += b.getHours() * hall.getType().getHourlyRate();
        }
        return total;
    }
}
