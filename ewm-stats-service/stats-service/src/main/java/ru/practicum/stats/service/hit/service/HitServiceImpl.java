package ru.practicum.stats.service.hit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.service.hit.model.EndpointStats;
import ru.practicum.stats.service.hit.model.Hit;
import ru.practicum.stats.service.hit.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;


    @Override
    public Hit addHit(Hit hit) {
        log.debug("Обработка запроса на сохранение хита {} в базу", hit);
        return hitRepository.save(hit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EndpointStats> getStats(String[] uris, LocalDateTime start, LocalDateTime end, Boolean unique) {
        log.debug("Обработка запроса на получение статистики по хитам за промежуток c {} по {}", start, end);
        return hitRepository.findEndpointStats(uris, start, end, unique);
    }

}
