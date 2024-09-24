package ru.practicum.ewm.user.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserDto {
    private String email;
    private String name;
    private Long id;
}
