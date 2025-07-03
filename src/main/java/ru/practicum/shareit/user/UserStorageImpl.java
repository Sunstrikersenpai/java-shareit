package ru.practicum.shareit.user;


import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserStorageImpl implements UserStorage {

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Optional<User> get(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User create(User user) {
        user.setId(id.getAndIncrement());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(Long id, User user) {
        users.put(id, user);
        return user;
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
