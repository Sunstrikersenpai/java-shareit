package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingListDto;

import java.util.List;

public interface CustomBookingRepository {
    List<BookingListDto> findBookingsByBookerOrOwnerWithState(
            Long userId, BookingState state, BookingServiceImpl.Role role
    );
}
