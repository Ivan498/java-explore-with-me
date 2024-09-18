package ru.practicum.ewm.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.util.Util;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {

    public ParticipationRequestDto toDto(Request request) {
        return ParticipationRequestDto.builder()
                .created(Util.toStringTime(request.getCreated().toLocalDateTime()))
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public List<ParticipationRequestDto> toDtoList(List<Request> requests) {
        return requests.stream().map(RequestMapper::toDto).collect(Collectors.toList());
    }

}
