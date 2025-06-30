package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingListDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

public interface BookingService {

    BookingDto create(Long userId, BookingShortDto bookingDto);

    BookingDto approveBooking(Long userId,Long bookingId,Boolean approved);

    BookingDto findBooking(Long userId,Long bookingId);

    List<BookingListDto> findAllBookingsByBooker(Long userId, BookingState state);

   List<BookingListDto>findAllBookingsByOwner(Long ownerId,BookingState state);
}
