package ru.practicum.stats.service.hit.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.stats.service.hit.model.EndpointStats;
import ru.practicum.stats.service.hit.model.Hit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class CustomizedHitRepositoryImpl implements CustomizedHitRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<EndpointStats> findEndpointStats(String[] uris, LocalDateTime start, LocalDateTime end, Boolean unique) {
        log.debug("Получение статистики по эндпоинтам {} в даты с {} по {} unique={}",
                Arrays.toString(uris),
                start,
                end, unique);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EndpointStats> cq = cb.createQuery(EndpointStats.class);
        Root<Hit> root = cq.from(Hit.class);

        List<Predicate> predicates = new ArrayList<>();

        if (uris != null && uris.length > 0) {
            predicates.add(root.get("uri").in((Object[]) uris));
        }

        predicates.add(cb.between(root.get("timestamp"), start, end));

        cq.multiselect(
                root.get("app"),
                root.get("uri"),
                unique ? cb.countDistinct(root.get("ip")) : cb.count(root.get("ip")))
                .where(predicates.toArray(new Predicate[0]))
                .groupBy(root.get("app"), root.get("uri"))
                .orderBy(cb.desc(unique ? cb.countDistinct(root.get("ip")) : cb.count(root.get("ip"))));

        List<EndpointStats> stats = entityManager.createQuery(cq).getResultList();
        return stats;
    }
}
