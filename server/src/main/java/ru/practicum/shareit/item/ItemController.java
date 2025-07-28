package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto create(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestBody ItemShortDto item) {
        return itemService.create(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long itemId,
            @RequestBody ItemShortDto item
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
        return itemService.findAllByOwnerId(ownerId);
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
        itemService.delete(itemId, ownerId);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto createComment(
            @Valid @RequestBody CommentCreateDto commentCreateDto,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId
    ) {
        return itemService.createComment(commentCreateDto, userId, itemId);
    }
}
