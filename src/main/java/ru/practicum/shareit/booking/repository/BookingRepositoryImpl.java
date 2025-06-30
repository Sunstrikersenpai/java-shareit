package ru.practicum.shareit.booking.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.QBooking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingListDto;

import java.time.LocalDateTime;
import java.util.List;

public class BookingRepositoryImpl implements CustomBookingRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<BookingListDto> findBookingsByBookerOrOwnerWithState(
            Long userId, BookingState state, BookingServiceImpl.Role role
    ) {
        QBooking b = QBooking.booking;
        LocalDateTime now = LocalDateTime.now();
        BooleanBuilder predicate = new BooleanBuilder();

        if (role.equals(BookingServiceImpl.Role.BOOKER)) {
            predicate.and(b.booker.id.eq(userId));
        } else {
            predicate.and(b.item.owner.id.eq(userId));
        }

        switch (state) {
            case CURRENT -> predicate
                    .and(b.status.eq(Status.APPROVED))
                    .and(b.start.before(now))
                    .and(b.end.after(now));
            case PAST -> predicate
                    .and(b.status.eq(Status.APPROVED))
                    .and(b.end.before(now));
            case FUTURE -> predicate
                    .and(b.status.eq(Status.APPROVED))
                    .and(b.start.after(now));
            case WAITING, REJECTED -> predicate
                    .and(b.status.eq(Status.valueOf(state.name())));
            case ALL -> {
            }
            default -> throw new IllegalArgumentException("Wrong state");
        }

        return new JPAQuery<>(em)
                .select(selectBookingDto(role))
                .from(b)
                .join(b.item).fetchJoin()
                .join(role == BookingServiceImpl.Role.BOOKER ? b.booker : b.item.owner).fetchJoin()
                .where(predicate)
                .orderBy(b.start.desc())
                .fetch();
    }

    private ConstructorExpression<BookingListDto> selectBookingDto(BookingServiceImpl.Role role) {
        QBooking b = QBooking.booking;

        return role == BookingServiceImpl.Role.BOOKER
                ? Projections.constructor(
                BookingListDto.class,
                b.id, b.start, b.end, b.status,
                b.item.id, b.item.name,
                b.booker.id, b.booker.name
        )
                : Projections.constructor(
                BookingListDto.class,
                b.id, b.start, b.end, b.status,
                b.item.id, b.item.name,
                b.item.owner.id, b.item.owner.name
        );
    }
}
