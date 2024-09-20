package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserService {

    User addUser(User user);

    List<User> getUsers(List<Long> userIds, Integer from, Integer size);

    void deleteUser(Long userId);

}
