package ru.practicum.stats.service.hit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.Util;
import ru.practicum.stats.service.hit.dto.HitMapper;
import ru.practicum.stats.service.hit.model.EndpointStats;
import ru.practicum.stats.service.hit.model.Hit;
import ru.practicum.stats.service.hit.service.HitService;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class HitController {
    private final HitService hitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void postHit(@RequestBody EndpointHit endpointHit) {
        log.info("Получен запрос на добавление хита");
        Hit hit = HitMapper.toHit(endpointHit);
        hitService.addHit(hit);
    }

    @GetMapping("/stats")
    public List<EndpointStats> getStats(@RequestParam(required = false) String[] uris,
                                        @RequestParam @NotBlank String start,
                                        @RequestParam @NotBlank String end,
                                        @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Получен запрос на просмотр статистики");
        LocalDateTime startDate = Util.decode(start);
        LocalDateTime endDate = Util.decode(end);
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Дата начала не может быть раньше конца диапазона");
        }
        return hitService.getStats(uris, startDate, endDate, unique);
    }

}
