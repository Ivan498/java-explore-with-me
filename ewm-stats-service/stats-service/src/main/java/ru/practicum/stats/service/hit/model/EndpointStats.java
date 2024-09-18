package ru.practicum.stats.service.hit.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EndpointStats {
    private String app;

    private String uri;

    private Long hits;
}
