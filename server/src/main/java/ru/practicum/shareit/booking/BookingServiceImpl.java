package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingDto create(Long bookerId, BookingShortDto bookingDto) {
        User booker = userService.getUserById(bookerId);
        Item item = itemService.getItemById(bookingDto.getItemId());
        if (!item.getAvailable()) {
            throw new UnavailableException("Item unavailable");
        }
        Booking booking = bookingRepository.save(BookingMapper.toEntity(bookingDto, booker, item));
        return BookingMapper.toDto(booking, UserMapper.toDto(booker), ItemMapper.toDto(item));
    }

    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, Boolean approved) {

        Booking booking = findBookingById(bookingId);
        User booker = booking.getBooker();
        Item item = booking.getItem();

        if (!item.getOwner().getId().equals(userId)) {
            throw new ValidationException("Wrong userId");
        }
        if (booking.getStatus() != Status.WAITING) {
            throw new ValidationException("Booking already processed");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
            item.setAvailable(false);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        itemService.updateWithItem(item);

        return BookingMapper.toDto(booking, UserMapper.toDto(booker), ItemMapper.toDto(item));
    }

    @Override
    public BookingDto findBooking(Long userId, Long bookingId) {
        User user = userService.getUserById(userId);
        Booking booking = findBookingById(bookingId);
        Item item = booking.getItem();

        if (!(user.equals(booking.getBooker()) || user.equals(item.getOwner()))) {
            throw new ValidationException("Bad request");
        }

        return BookingMapper.toDto(booking, UserMapper.toDto(user), ItemMapper.toDto(item));
    }

    @Override
    public List<BookingDto> findAllBookingsByBooker(Long userId, BookingState state) {
        userService.getUserById(userId);
        List<Booking> bookings = bookingRepository.findBookingsByBookerOrOwnerWithState(userId, state, Role.BOOKER);

        return bookings.stream().map(BookingMapper::toDto).toList();
    }

    @Override
    public List<BookingDto> findAllBookingsByOwner(Long ownerId, BookingState state) {
        List<Item> items = itemService.findAllEntityByOwner(ownerId);
        if (items == null) {
            return List.of();
        } else {
            List<Booking> bookings = bookingRepository.findBookingsByBookerOrOwnerWithState(ownerId, state, Role.OWNER);
            return bookings.stream()
                    .map(BookingMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    public Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
    }

    public enum Role {
        OWNER,
        BOOKER
    }
}
