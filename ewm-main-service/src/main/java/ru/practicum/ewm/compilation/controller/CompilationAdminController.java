package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Получен запрос на добавление подборки {}", newCompilationDto.toString());
        return compilationService.addCompilation(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patchCompilation(@PathVariable Long compId,
                                           @Valid @RequestBody UpdateCompilationRequest update) {
        log.info("Получен запрос на изменение подборки с id={}: {}", compId, update.toString());
        return compilationService.updateCompilation(compId, update);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Получен запрос на удаление подборки с id={}", compId);
        compilationService.deleteCompilation(compId);
    }
}
