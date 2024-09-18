package ru.practicum.ewm.compilation.dto;


import lombok.*;

import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UpdateCompilationRequest {

    private List<Long> events = Collections.emptyList();

    private Boolean pinned;

    @Size(min = 1, max = 50)
    private String title;

}
