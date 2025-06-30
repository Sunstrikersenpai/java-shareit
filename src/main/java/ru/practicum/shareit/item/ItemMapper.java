package ru.practicum.shareit.item;

import java.util.List;

public class ItemMapper {

    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .description(item.getDescription())
                .id(item.getId())
                .name(item.getName())
                .available(item.getAvailable())
                .build();
    }

    public static Item toEntity(ItemDto itemDto) {
        return Item.builder()
                .available(itemDto.getAvailable())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .build();
    }

    public static List<ItemDto> toDto(List<Item> items) {
        return items.stream().map(ItemMapper::toDto).toList();
    }
}
