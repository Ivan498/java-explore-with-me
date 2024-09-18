package ru.practicum.stats.service.hit.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.stats.service.hit.model.EndpointStats;
import ru.practicum.stats.service.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CustomizedHitRepositoryImplTest {

    private final HitRepository hitRepository;

    @BeforeEach
    void addHits() {
        hitRepository.save(Hit.builder()
                .app("ewm-main-service")
                .uri("/events/1")
                .ip("192.163.0.1")
                .timestamp(LocalDateTime.of(2022, 9, 6, 11, 0, 21))
                .build());

        hitRepository.save(Hit.builder()
                .app("ewm-main-service")
                .uri("/events/2")
                .ip("192.163.0.1")
                .timestamp(LocalDateTime.of(2022, 9, 6, 11, 0, 22))
                .build());

        hitRepository.save(Hit.builder()
                .app("ewm-main-service")
                .uri("/events/1")
                .ip("192.163.0.1")
                .timestamp(LocalDateTime.of(2022, 9, 6, 11, 0, 23))
                .build());

        hitRepository.save(Hit.builder()
                .app("ewm-main-service")
                .uri("/events/3")
                .ip("192.163.0.1")
                .timestamp(LocalDateTime.of(2022, 9, 6, 11, 0, 23))
                .build());
    }

    @AfterEach
    void clearDb() {
        hitRepository.deleteAll();
    }

    @Test
    void findEndpointStats_whenInvoked_ReturnsListOfEndpointStats() {
        String[] uris = new String[]{"/events/1", "/events/2"};
        LocalDateTime start = LocalDateTime.of(2022, 8, 6, 11, 0, 21);
        LocalDateTime end = LocalDateTime.of(2022, 10, 6, 11, 0, 21);
        Boolean unique = false;

        List<EndpointStats> stats = hitRepository.findEndpointStats(uris, start, end, unique);

        assertTrue(!stats.isEmpty());
        assertEquals(2, stats.size());
        assertEquals(2, stats.get(0).getHits());
    }

    @Test
    void findEndpointStats_whenInvokedWithNullUris_ReturnsListOfAllEndpointStats() {
        String[] uris = null;
        LocalDateTime start = LocalDateTime.of(2022, 8, 6, 11, 0, 21);
        LocalDateTime end = LocalDateTime.of(2022, 10, 6, 11, 0, 21);
        Boolean unique = false;

        List<EndpointStats> stats = hitRepository.findEndpointStats(uris, start, end, unique);

        assertTrue(!stats.isEmpty());
        assertEquals(3, stats.size());

    }

    @Test
    void findEndpointStats_whenInvokedAndUniqueTrue_ReturnsEndpointStatsListWithCorrectNumberOfHits() {
        String[] uris = new String[]{"/events/1"};
        LocalDateTime start = LocalDateTime.of(2022, 8, 6, 11, 0, 21);
        LocalDateTime end = LocalDateTime.of(2022, 10, 6, 11, 0, 21);
        Boolean unique = true;

        List<EndpointStats> stats = hitRepository.findEndpointStats(uris, start, end, unique);

        assertTrue(!stats.isEmpty());
        assertEquals(1, stats.size());
        assertEquals(1, stats.get(0).getHits());

    }
}