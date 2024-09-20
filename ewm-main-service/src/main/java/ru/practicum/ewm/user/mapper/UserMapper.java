package ru.practicum.ewm.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public User toUser(NewUserRequest newUserRequest) {
        return User.builder()
                .email(newUserRequest.getEmail())
                .name(newUserRequest.getName())
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .id(user.getId())
                .build();
    }

    public List<UserDto> toUserDtoList(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
