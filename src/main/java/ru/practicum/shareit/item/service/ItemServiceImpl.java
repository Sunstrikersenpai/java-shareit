package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;


    @Override
    public ItemDto create(Long ownerId, ItemDto itemDto) {
        Item item = ItemMapper.toEntity(itemDto);
        item.setOwner(userService.getUserById(ownerId));

        return ItemMapper.toDto(itemStorage.create(item));
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, ItemDto itemDto) {
        Item item = getItemById(itemId);
        if (!ownerId.equals(item.getOwner().getId())) {
            throw new NotFoundException("Owner not found");
        }
        patchItem(item, itemDto);
        return ItemMapper.toDto(itemStorage.update(itemId, item));
    }

    @Override
    public ItemDto get(Long itemId) {
        return ItemMapper.toDto(getItemById(itemId));
    }

    @Override
    public List<ItemDto> findAllByOwner(Long ownerId) {
        userService.getUserById(ownerId);
        List<Item> items = itemStorage.findAllByOwnerId(ownerId);

        return ItemMapper.toDto(items);
    }

    @Override
    public List<ItemDto> search(String text) {
        List<Item> items = itemStorage.searchAvailableByText(text.toLowerCase());
        return ItemMapper.toDto(items);
    }

    @Override
    public void delete(Long itemId,Long ownerId) {
        Item item = getItemById(itemId);
        if (!ownerId.equals(item.getOwner().getId())) {
            throw new ValidationException("Wrong owner id");
        }
        itemStorage.delete(itemId);
    }

    private Item getItemById(Long itemId) {
        return itemStorage.get(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
    }

    private void patchItem(Item item, ItemDto itemDto) {
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
    }
}
