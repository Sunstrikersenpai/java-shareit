package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.BookingState;

import java.util.List;

public interface CustomBookingRepository {
    List<Booking> findBookingsByBookerOrOwnerWithState(
            Long userId, BookingState state, BookingServiceImpl.Role role
    );
}
