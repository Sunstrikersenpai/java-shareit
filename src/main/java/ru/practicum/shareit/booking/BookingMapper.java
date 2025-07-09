package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;

public class BookingMapper {

    public static BookingDto toDto(Booking booking, UserDto booker, ItemDto item) {
        return BookingDto.builder().id(booking.getId()).status(booking.getStatus())
                .startTime(booking.getStartTime()).endTime(booking.getEndTime()).booker(booker).item(item).build();
    }

    public static Booking toEntity(BookingShortDto dto, User booker, Item item) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStartTime(dto.getStartTime());
        booking.setEndTime(dto.getEndTime());
        return booking;
    }

    public static BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .booker(UserMapper.toDto(booking.getBooker()))
                .item(ItemMapper.toDto(booking.getItem()))
                .id(booking.getId())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .status(booking.getStatus())
                .build();
    }
}
