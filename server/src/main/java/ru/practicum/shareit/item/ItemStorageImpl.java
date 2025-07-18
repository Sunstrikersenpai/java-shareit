package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemStorageImpl implements ItemStorage {

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item create(Item item) {
        item.setId(id.getAndIncrement());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Long itemId, Item item) {
        items.put(itemId, item);
        return item;
    }

    @Override
    public Optional<Item> get(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> findAllByOwnerId(Long ownerId) {
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), ownerId))
                .toList();
    }

    @Override
    public List<Item> searchAvailableByText(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        List<Item> searchedItems = new ArrayList<>();

        for (Item item : items.values()) {
            if (item.getAvailable()) {
                if (
                        item.getDescription().toLowerCase().contains(text)
                                || item.getName().toLowerCase().contains(text)
                ) {
                    searchedItems.add(item);
                }
            }
        }
        return searchedItems;
    }

    @Override
    public void delete(Long itemId) {
        items.remove(itemId);
    }

    @Override
    public void deleteItemsByUserId(Long userId) {
        List<Long> idsToRemove = items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(Item::getId)
                .toList();

        for (Long id : idsToRemove) {
            items.remove(id);
        }
    }
}