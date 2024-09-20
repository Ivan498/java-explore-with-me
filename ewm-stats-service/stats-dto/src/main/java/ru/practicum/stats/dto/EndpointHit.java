package ru.practicum.stats.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EndpointHit {
    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
