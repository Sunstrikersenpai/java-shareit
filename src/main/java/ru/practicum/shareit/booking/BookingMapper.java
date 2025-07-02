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

    public static BookingDto toDto(Booking obj, UserDto booker, ItemDto item) {
        return BookingDto.builder().id(obj.getId()).status(obj.getStatus())
                .start(obj.getStart()).end(obj.getEnd()).booker(booker).item(item).build();
    }

    public static Booking toEntity(BookingShortDto dto, User booker, Item item) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        return booking;
    }

    public static BookingDto toDto(Booking obj) {
        return BookingDto.builder()
                .booker(UserMapper.toDto(obj.getBooker()))
                .item(ItemMapper.toDto(obj.getItem()))
                .id(obj.getId())
                .start(obj.getStart())
                .end(obj.getEnd())
                .status(obj.getStatus())
                .build();
    }
}
