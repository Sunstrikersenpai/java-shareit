package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, CustomBookingRepository {
    @Query(value = """
            SELECT DISTINCT ON(i.id) b.*
            FROM bookings as b
            JOIN items as i on b.item_id = i.id
            WHERE i.id IN (?1)
            AND b.end_time < now()
            AND b.status = 'APPROVED'
            ORDER BY i.id,b.end_time desc
            """, nativeQuery = true)
    List<Booking> findLastBookings(List<Long> itemIds);

    @Query(value = """
            SELECT DISTINCT ON(i.id) b.*
            FROM bookings as b
            JOIN items as i on b.item_id = i.id
            WHERE i.id IN (?1)
            AND now() < b.start_time
            AND b.status = 'APPROVED'
            ORDER BY i.id,b.start_time asc
            """, nativeQuery = true)
    List<Booking> findNextBookings(List<Long> itemIds);

    boolean existsByBookerIdAndItemIdAndEndBefore(
            Long bookerId, Long itemId, LocalDateTime now
    );
}
