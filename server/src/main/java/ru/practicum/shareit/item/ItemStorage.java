package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item create(Item item);

    Item update(Long itemId, Item item);

    Optional<Item> get(Long itemId);

    List<Item> findAllByOwnerId(Long ownerId);

    List<Item> searchAvailableByText(String text);

    void delete(Long itemId);

    void deleteItemsByUserId(Long userId);
}