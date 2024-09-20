package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.util.Util;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(User user) {
        log.debug("Обработка запроса на добавление пользователя: {}", user.toString());
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers(List<Long> userIds, Integer from, Integer size) {
        log.debug("Обработка запроса на получение всех пользователей по id");
        if (userIds != null) {
            return userIds.size() > 0 ? userRepository.findAllByIdIn(userIds) : Collections.emptyList();
        } else {
            return userRepository.findAllBy(Util.page(from, size));
        }
    }

    @Override
    public void deleteUser(Long userId) {
        log.debug("Обработка запроса на удаление пользователя по id={}", userId);
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        userRepository.deleteById(userId);
    }
}
