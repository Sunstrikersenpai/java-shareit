package ru.practicum.shareit.booking.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.*;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public class BookingRepositoryImpl implements CustomBookingRepository {

    @PersistenceContext
    private EntityManager em;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Booking> findBookingsByBookerOrOwnerWithState(
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

        return queryFactory
                .selectFrom(b)
                .join(b.item).fetchJoin()
                .join(b.booker).fetchJoin()
                .where(predicate)
                .orderBy(b.start.desc())
                .fetch();
    }
}
