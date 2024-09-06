package ru.practicum.stats.service.hit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.Util;
import ru.practicum.stats.service.hit.dto.HitMapper;
import ru.practicum.stats.service.hit.model.EndpointStats;
import ru.practicum.stats.service.hit.model.Hit;
import ru.practicum.stats.service.hit.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HitController {
    private final HitService hitService;

    @PostMapping("/hit")
    public void postHit(@RequestBody EndpointHit endpointHit) {
        log.info("Получен запрос на добавление хита");
        Hit hit = HitMapper.toHit(endpointHit);
        hitService.addHit(hit);
    }

    @GetMapping("/stats")
    public List<EndpointStats> getStats(@RequestParam(required = false) String[] uris,
                                        @RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Получен запрос на просмотр статистики");
        LocalDateTime startDate = Util.toLocalDateTime(start);
        LocalDateTime endDate = Util.toLocalDateTime(end);
        return hitService.getStats(uris, startDate, endDate, unique);
    }

}
