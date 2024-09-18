package ru.practicum.stats.service.hit.repository;

import ru.practicum.stats.service.hit.model.EndpointStats;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomizedHitRepository {
    List<EndpointStats> findEndpointStats(String[] uris, LocalDateTime start, LocalDateTime end, Boolean unique);
}
