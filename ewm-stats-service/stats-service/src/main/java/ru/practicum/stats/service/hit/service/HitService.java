package ru.practicum.stats.service.hit.service;

import ru.practicum.stats.service.hit.model.EndpointStats;
import ru.practicum.stats.service.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    Hit addHit(Hit hit);

    List<EndpointStats> getStats(String[] uris, LocalDateTime start, LocalDateTime end, Boolean unique);
}
