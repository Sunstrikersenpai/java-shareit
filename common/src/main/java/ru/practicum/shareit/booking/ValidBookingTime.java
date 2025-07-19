package ru.practicum.shareit.booking;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Constraint(validatedBy = BookingTimeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBookingTime {
    String[] message() default "Начало должно быть после конца";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}