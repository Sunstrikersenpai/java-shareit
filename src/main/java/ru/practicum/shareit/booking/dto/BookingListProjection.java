package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

public interface BookingListProjection {

    Long getId();
    LocalDateTime getStart();
    LocalDateTime getEnd();
    Status getStatus();

    ItemProjection getItem();
    UserProjection getBooker();

    interface ItemProjection {
        Long getId();
        String getName();
    }

    interface UserProjection {
        Long getId();
        String getName();
    }
}
