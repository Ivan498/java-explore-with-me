package ru.practicum.ewm.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class NewCategoryDto {

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
