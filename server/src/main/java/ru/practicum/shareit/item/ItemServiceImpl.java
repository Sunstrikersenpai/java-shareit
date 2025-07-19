package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;


    @Override
    public ItemDto create(Long ownerId, ItemShortDto itemShortDto) {
        Item item = ItemMapper.toEntity(itemShortDto);
        item.setOwner(userService.getUserById(ownerId));

        if (itemShortDto.getRequestId() != null) {
            ItemRequest request = itemRequestRepository.findById(itemShortDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос не найден"));
            item.setRequest(request);
        }

        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, ItemShortDto itemShortDto) {
        Item item = getItemById(itemId);
        if (!ownerId.equals(item.getOwner().getId())) {
            throw new NotFoundException("Owner not found");
        }
        patchItem(item, itemShortDto);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public void updateWithItem(Item item) {
        itemRepository.save(item);
    }

    @Override
    public ItemDto get(Long itemId) {
        ItemDto dto = ItemMapper.toDto(getItemById(itemId));
        dto.setComments(CommentMapper.toDto(commentRepository.findAllByItemId(itemId)));
        return dto;
    }

    @Override
    public List<ItemDto> findAllByOwnerId(Long ownerId) {
        userService.getUserById(ownerId);
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        List<Long> itemIds = items.stream().map(Item::getId).toList();
        List<Booking> lastBookings = bookingRepository.findLastBookings(itemIds);
        Map<Long, Booking> lastBookingsMap = lastBookings.stream()
                .collect(Collectors.toMap(
                        (Booking booking) -> booking.getItem().getId(),
                        Function.identity()
                ));

        List<Booking> nextBookings = bookingRepository.findNextBookings(itemIds);
        Map<Long, Booking> nextBookingsMap = nextBookings.stream()
                .collect(Collectors.toMap(
                        (Booking booking) -> booking.getItem().getId(),
                        Function.identity()
                ));
        List<ItemDto> dtoList = ItemMapper.toDto(items, nextBookingsMap, lastBookingsMap);
        dtoList.forEach(dto -> {
            List<Comment> comments = commentRepository.findAllByItemId(dto.getId());
            dto.setComments(CommentMapper.toDto(comments));
        });
        return dtoList;
    }

    @Override
    public List<Item> findAllEntityByOwner(Long ownerId) {
        userService.getUserById(ownerId);

        return itemRepository.findAllByOwnerId(ownerId);
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
    public void delete(Long itemId, Long ownerId) {
        Item item = getItemById(itemId);
        if (!ownerId.equals(item.getOwner().getId())) {
            throw new ValidationException("Wrong owner id");
        }
        itemRepository.delete(item);
    }

    @Override
    public CommentDto createComment(CommentCreateDto commentCreateDto, Long userId, Long itemId) {
        User user = userService.getUserById(userId);
        Item item = getItemById(itemId);

        if (!bookingRepository.existsByBookerIdAndItemIdAndEndTimeBefore(
                userId, itemId, LocalDateTime.now()
        )) {
            throw new ValidationException("No booking for user");
        }

        Comment comment = commentRepository.save(CommentMapper.toEntity(commentCreateDto, user, item));

        return CommentMapper.toDto(comment);
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
    }

    private void patchItem(Item item, ItemShortDto itemShortDto) {
        if (itemShortDto.getAvailable() != null) {
            item.setAvailable(itemShortDto.getAvailable());
        }
        if (itemShortDto.getDescription() != null) {
            item.setDescription(itemShortDto.getDescription());
        }
        if (itemShortDto.getName() != null) {
            item.setName(itemShortDto.getName());
        }
    }
}
