package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestBody BookingShortDto bookingDto
    ) {
        return bookingClient.create(bookerId, bookingDto);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> approveBooking(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long bookingId,
            @RequestParam("approved") Boolean approved
    ) {
        return bookingClient.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> findBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId
    ) {
        return bookingClient.findBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookingsByUserId(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        return bookingClient.findAllBookingsByUserId(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllBookingsByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        return bookingClient.findAllBookingsByOwnerId(ownerId, state);
    }
}
