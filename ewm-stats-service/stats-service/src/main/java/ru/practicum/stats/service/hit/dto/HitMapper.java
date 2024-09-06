package ru.practicum.stats.service.hit.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.Util;
import ru.practicum.stats.service.hit.model.Hit;

@UtilityClass
public class HitMapper {
    public static Hit toHit(EndpointHit endpointHit) {
        return Hit.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(Util.toLocalDateTime(endpointHit.getTimestamp()))
                .build();
    }

    public static EndpointHit toEndpointHit(Hit hit) {
        return EndpointHit.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(Util.toStringTime(hit.getTimestamp()))
                .build();
    }
}
