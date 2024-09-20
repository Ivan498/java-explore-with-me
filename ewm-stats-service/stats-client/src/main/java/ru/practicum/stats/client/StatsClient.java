package ru.practicum.stats.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.Util;
import ru.practicum.stats.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${ewm-stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createHit(EndpointHit endpointHit) {
        return post("/hit", endpointHit);
    }

    public ResponseEntity<List<ViewStats>> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        String startDate = Util.encode(start);
        String endDate = Util.encode(end);
        Map<String, Object> parameters = Map.of(
                "start", startDate,
                "end", endDate,
                "uris", uris,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                parameters,
                new ParameterizedTypeReference<List<ViewStats>>() {});
    }

}
