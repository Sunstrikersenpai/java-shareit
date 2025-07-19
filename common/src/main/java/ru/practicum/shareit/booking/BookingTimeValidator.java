package ru.practicum.shareit.booking;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookingTimeValidator implements ConstraintValidator<ValidBookingTime, BookingDto> {
    @Override
    public boolean isValid(BookingDto bookingDto, ConstraintValidatorContext context) {
        return bookingDto.getStartTime().isBefore(bookingDto.getEndTime());
    }
}
