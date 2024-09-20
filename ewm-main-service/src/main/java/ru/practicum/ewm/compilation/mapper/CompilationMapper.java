package ru.practicum.ewm.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public CompilationDto toDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .events(events)
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public Compilation updateCompilation(Compilation compilation, UpdateCompilationRequest update, List<Event> events) {
        return Compilation.builder()
                .id(compilation.getId())
                .events(events)
                .pinned(update.getPinned() == null ? false : update.getPinned())
                .title(update.getTitle() == null ? compilation.getTitle() : update.getTitle())
                .build();
    }

    public static List<CompilationDto> toDtoList(List<Compilation> compilations, List<EventShortDto> eventShortDtoList) {
        Map<Long, EventShortDto> eventShortDtoMap = eventShortDtoList.stream()
                .collect(Collectors.toMap(EventShortDto::getId, esd -> esd));

        return compilations.stream()
                .map(c -> toDto(c, c.getEvents().stream()
                        .map(e -> eventShortDtoMap.get(e.getId()))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
}
