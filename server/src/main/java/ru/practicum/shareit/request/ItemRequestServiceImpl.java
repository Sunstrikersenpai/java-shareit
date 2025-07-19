package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto createRequest(Long userId, ItemRequestShortDto requestDto) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto, user, LocalDateTime.now());
        ItemRequest saved = requestRepository.save(request);

        return ItemRequestMapper.toResponseDto(saved, List.of());
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }

        List<ItemRequest> requests = requestRepository.findByRequestorIdOrderByCreatedDesc(userId);

        return mapRequestsWithItems(requests);
    }

    @Override
    public List<ItemRequestDto> getOtherUsersRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }

        List<ItemRequest> requests = requestRepository.findByRequestorIdNotOrderByCreatedDesc(userId);

        return mapRequestsWithItems(requests);
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }

        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));

        List<Item> items = itemRepository.findByRequestId(requestId);
        return ItemRequestMapper.toResponseDto(request, items);
    }

    private List<ItemRequestDto> mapRequestsWithItems(List<ItemRequest> requests) {
        return requests.stream()
                .map(req -> {
                    List<Item> items = itemRepository.findByRequestId(req.getId());
                    return ItemRequestMapper.toResponseDto(req, items);
                })
                .collect(Collectors.toList());
    }
}
