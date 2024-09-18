package ru.practicum.ewm.request.dto;

import lombok.*;
import ru.practicum.ewm.request.model.RequestStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ParticipationRequestDto {
    private String created;

    private Long event;

    private Long id;

    private Long requester;

    private RequestStatus status;
}
