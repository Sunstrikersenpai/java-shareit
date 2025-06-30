package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingListDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private BookingServiceImpl bookingService;

    @PostMapping
    public BookingDto create(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestBody BookingShortDto bookingDto
    ) {
        return bookingService.create(bookerId,bookingDto);
    }

    @PatchMapping("{bookingId}")
    public BookingDto approveBooking(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long bookingId,
            @RequestParam("approved") Boolean approved
    ) {
        return bookingService.approveBooking(ownerId,bookingId,approved);
    }

    @GetMapping("{bookingId}")
    public BookingDto findBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId
    ) {
        return bookingService.findBooking(userId,bookingId);
    }

    @GetMapping
    public List<BookingListDto> findAllBookingsByUserId(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        return bookingService.findAllBookingsByBooker(userId,state);
    }

    @GetMapping("/owner")
    public List<BookingListDto> findAllBookingsByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        return bookingService.findAllBookingsByOwner(ownerId,state);
    }
}
