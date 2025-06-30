package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;


    @Override
    public ItemDto create(Long ownerId, ItemDto itemDto) {
        Item item = ItemMapper.toEntity(itemDto);
        item.setOwner(userService.getUserById(ownerId));

        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, ItemDto itemDto) {
        Item item = getItemById(itemId);
        if (!ownerId.equals(item.getOwner().getId())) {
            throw new NotFoundException("Owner not found");
        }
        patchItem(item, itemDto);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto get(Long itemId) {
        return ItemMapper.toDto(getItemById(itemId));
    }

    @Override
    public List<ItemDto> findAllByOwnerId(Long ownerId) {
        userService.getUserById(ownerId);
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);

        return ItemMapper.toDto(items);
    }

    @Override
    public List<Item> findAllEntityByOwner(Long ownerId) {
        userService.getUserById(ownerId);
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);

        return items;
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        List<Item> items = itemRepository.findAvailableByText(text.toLowerCase());
        return ItemMapper.toDto(items);
    }

    @Override
    public void delete(Long itemId,Long ownerId) {
        Item item = getItemById(itemId);
        if (!ownerId.equals(item.getOwner().getId())) {
            throw new ValidationException("Wrong owner id");
        }
        itemRepository.delete(item);
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
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
