package ru.practicum.stats.service.hit.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.stats.service.hit.model.Hit;

public interface HitRepository extends CrudRepository<Hit, Long>, CustomizedHitRepository {
}
