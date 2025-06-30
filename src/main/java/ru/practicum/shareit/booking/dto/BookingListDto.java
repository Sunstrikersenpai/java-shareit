package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingListDto {
    private Long id;
    private ItemDto item;
    private UserDto booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;

    public BookingListDto(Long id, LocalDateTime start, LocalDateTime end, Status status,
                          Long itemId, String itemName,
                          Long bookerId, String bookerName) {
        this.id = id;
        this.item = new ItemDto(itemId,itemName);
        this.booker = new UserDto(bookerId,bookerName);
        this.start = start;
        this.end = end;
        this.status = status;
    }
}
