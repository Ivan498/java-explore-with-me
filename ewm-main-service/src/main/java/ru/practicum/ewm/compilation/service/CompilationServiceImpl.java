package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        log.info("Обработка запроса на добавление подборки {}", newCompilationDto.toString());
        List<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());

        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        Compilation savedComp = compilationRepository.save(compilation);
        List<EventShortDto>  eventShortDtoList = eventService.makeEventShortDtoList(events);

        return CompilationMapper.toDto(savedComp, eventShortDtoList);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest update) {
        log.info("Обработка запроса на изменение подборки с id={}: {}", compId, update.toString());
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(Compilation.class, compId));
        List<Event> events = eventRepository.findAllByIdIn(update.getEvents());

        Compilation compilationToSave = CompilationMapper.updateCompilation(compilation, update, events);
        Compilation savedComp = compilationRepository.save(compilationToSave);

        List<EventShortDto>  eventShortDtoList = eventService.makeEventShortDtoList(events);

        return CompilationMapper.toDto(savedComp, eventShortDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Обработка запроса на просмотр pinned={} подборок событий с {} по {}", pinned, from, size);
        if (pinned == null) {
            pinned = false;
        }
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, Util.page(from, size));
        Set<Event> events = compilations.stream().map(Compilation::getEvents).flatMap(List::stream).collect(Collectors.toSet());
        List<EventShortDto> eventShortDtoList = eventService.makeEventShortDtoList(new ArrayList<>(events));

        return CompilationMapper.toDtoList(compilations, eventShortDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(Long compId) {
        log.info("Обработка запроса на просмотр подборки с id={}", compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(Compilation.class, compId));
        List<EventShortDto> eventShortDtoList = eventService.makeEventShortDtoList(compilation.getEvents());
        return CompilationMapper.toDto(compilation, eventShortDtoList);
    }

    @Override
    public void deleteCompilation(Long compId) {
        log.info("Обработка запроса на удаление подборки с id={}", compId);
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(Compilation.class, compId));
        compilationRepository.deleteById(compId);
    }
}
