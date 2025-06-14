package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto create(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestBody @Valid ItemDto item) {
        return itemService.create(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long itemId,
            @RequestBody ItemDto item
    ) {
        return itemService.update(ownerId, itemId, item);
    }

    @GetMapping("{itemId}")
    public ItemDto get(
            @PathVariable Long itemId
    ) {
        return itemService.get(itemId);
    }

    @GetMapping
    public List<ItemDto> findAllByOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemService.findAllByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(
            @RequestParam String text
    ) {
        return itemService.search(text);
    }

    @DeleteMapping("/{itemId}")
    public void delete(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        itemService.delete(itemId,ownerId);
    }
}
