package ru.practicum.ewm.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NewCommentDto {

    @NotBlank
    @Size(min = 1, max = 5000)
    private String text;
}
