package com.hallsymphony.service;

import com.hallsymphony.model.Booking;
import com.hallsymphony.model.Hall;
import com.hallsymphony.repo.BookingRepository;
import com.hallsymphony.repo.HallRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingService {
    private final BookingRepository bookingRepository;
    private final HallRepository hallRepository;

    public BookingService(BookingRepository bookingRepository, HallRepository hallRepository) {
        this.bookingRepository = bookingRepository;
        this.hallRepository = hallRepository;
    }

    public List<Hall> allHalls() { return hallRepository.findAll(); }
    public List<Booking> allBookings() { return bookingRepository.findAll(); }

    public Booking book(String customerId, String hallId, LocalDateTime start, LocalDateTime end) {
        if (start.isBefore(LocalDateTime.now()) || !end.isAfter(start)) {
            throw new IllegalArgumentException("Invalid booking date/time.");
        }
        Booking booking = new Booking(bookingRepository.nextId(), customerId, hallId, start, end, false);
        bookingRepository.save(booking);
        return booking;
    }

    public void cancel(Booking booking) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), booking.getStart().toLocalDate());
        if (days < 3) throw new IllegalArgumentException("Cancellation only allowed at least 3 days before booking date.");
        booking.setCancelled(true);
        bookingRepository.save(booking);
    }
}
