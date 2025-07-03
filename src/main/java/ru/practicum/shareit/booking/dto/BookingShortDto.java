package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.ValidBookingTime;

import java.time.LocalDateTime;

@ValidBookingTime
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingShortDto {
    @NotNull
    private Long itemId;

    @NotNull
    @Future
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;
}
