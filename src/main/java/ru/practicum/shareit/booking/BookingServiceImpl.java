package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingListDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.util.List;

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
        Booking booking = bookingRepository.save(BookingMapper.toEntity(bookingDto, booker, item));
        return BookingMapper.toDto(booking, UserMapper.toDto(booker), ItemMapper.toDto(item));
    }

    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, Boolean approved) {
        User user = userService.getUserById(userId);
        Booking booking = findBookingById(bookingId);
        Item item = booking.getItem();

        if (!item.getOwner().equals(user)) {
            throw new ValidationException("Wrong userId");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return BookingMapper.toDto(booking, UserMapper.toDto(user), ItemMapper.toDto(item));
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
    public List<BookingListDto> findAllBookingsByBooker(Long userId, BookingState state) {
        userService.getUserById(userId);
        return bookingRepository.findBookingsByBookerOrOwnerWithState(userId,state,Role.BOOKER);
       /* switch (state) {
            case ALL -> {
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            }
            case CURRENT -> {
                return bookingRepository.findCurrentBookings(userId);
            }
            case PAST -> {
                return bookingRepository.findPastBookings(userId);
            }
            case FUTURE -> {
                return bookingRepository.findFutureBookings(userId);
            }
            case WAITING, REJECTED -> {
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.valueOf(state.toString()));
            }
            default -> throw new IllegalArgumentException("Wrong state");
        }*/
    }


    public List<BookingListDto> findAllBookingsByOwner(Long ownerId, BookingState state) {
        List<Item> items = itemService.findAllEntityByOwner(ownerId);
        if (items == null) {
            return List.of();
        } else {
            return bookingRepository.findBookingsByBookerOrOwnerWithState(ownerId,state,Role.OWNER);
        }
    }

    public Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
    }

    public enum Role{
        OWNER,
        BOOKER
    }
}
