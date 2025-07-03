package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemMapper {

    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .description(item.getDescription())
                .id(item.getId())
                .name(item.getName())
                .available(item.getAvailable())
                .build();
    }

    public static Item toEntity(ItemShortDto itemShortDto) {
        return Item.builder()
                .available(itemShortDto.getAvailable())
                .name(itemShortDto.getName())
                .description(itemShortDto.getDescription())
                .build();
    }

    public static List<ItemDto> toDto(List<Item> items) {
        return items.stream().map(ItemMapper::toDto).toList();
    }

    public static List<ItemDto> toDto(
            List<Item> items,
            Map<Long, Booking> nextBookingsMap,
            Map<Long, Booking> lastBookingsMap
    ) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : items) {
            Booking next = nextBookingsMap.get(item.getId());
            Booking last = lastBookingsMap.get(item.getId());

            ItemDto dto = ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .nextBooking(next != null ? BookingMapper.toDto(next) : null)
                    .lastBooking(last != null ? BookingMapper.toDto(last) : null)
                    .build();
            itemDtoList.add(dto);
        }
        return itemDtoList;
    }
}
