package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingListProjection;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, CustomBookingRepository {

    List<BookingListProjection> findAllByBookerIdOrderByStartDesc(Long userId);

  /*  @Query("""
                SELECT b FROM Booking b
                JOIN b.item i
                JOIN b.booker u
                WHERE b.booker.id = ?1
                AND b.status = 'APPROVED'
                AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end
                ORDER BY b.start DESC
            """)
    List<BookingListProjection> findCurrentBookings(Long userId);

    @Query("""
                SELECT b FROM Booking b
                JOIN b.item i
                JOIN b.booker u
                WHERE b.booker.id = ?1
                AND b.status = 'APPROVED'
                AND b.end < CURRENT_TIMESTAMP
                ORDER BY b.start DESC
            """)
    List<BookingListProjection> findPastBookings(Long userId);


    @Query("""
                SELECT b FROM Booking b
                JOIN b.item i
                JOIN b.booker u
                WHERE b.booker.id = ?1
                AND b.status = 'APPROVED'
                AND b.start > CURRENT_TIMESTAMP
                ORDER BY b.start DESC
            """)
    List<BookingListProjection> findFutureBookings(Long userId);

    List<BookingListProjection> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, Status status);*/
}
