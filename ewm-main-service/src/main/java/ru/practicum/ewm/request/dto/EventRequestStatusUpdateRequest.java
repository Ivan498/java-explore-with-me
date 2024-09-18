package ru.practicum.ewm.request.dto;

import lombok.*;
import ru.practicum.ewm.request.model.RequestStatus;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private RequestStatus status;
}
