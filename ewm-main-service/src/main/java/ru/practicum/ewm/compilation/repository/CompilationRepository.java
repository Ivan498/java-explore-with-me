package ru.practicum.ewm.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends CrudRepository<Compilation, Long> {

    List<Compilation> findAllByPinned(Boolean pinned, Pageable page);
}
