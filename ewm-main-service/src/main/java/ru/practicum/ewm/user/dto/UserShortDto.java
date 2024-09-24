package ru.practicum.ewm.user.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class UserShortDto {
    private Long id;
    private String name;
}
