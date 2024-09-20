package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomizedEventRepositoryImpl implements CustomizedEventRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Event> findAllByAdminParams(Long[] users, EventState[] states, Long[] categories, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, Pageable page) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();

        if (users != null && users.length > 0) {
            predicates.add(root.get("initiator").in((Object[]) users));
        }

        if (states != null && states.length > 0) {
            predicates.add(root.get("state").in((Object[]) states));
        }

        if (categories != null && categories.length > 0) {
            predicates.add(root.get("category").in((Object[]) categories));
        }

        if (rangeStart != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }

        if (rangeEnd != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(QueryUtils.toOrders(page.getSort(), root, cb));

        List<Event> events = entityManager.createQuery(cq)
                .setMaxResults(page.getPageSize())
                .getResultList();
        return events;
    }

    @Override
    public List<Event> findAllByPublicParams(String text, Long[] categories, Boolean paid, LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable page) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("state"), EventState.PUBLISHED));

        if (text != null && !text.isBlank()) {
            predicates.add(cb.or(
                    cb.like(cb.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("description")), "%" + text.toLowerCase() + "%")
            ));
        }

        if (categories != null && categories.length > 0) {
            predicates.add(root.get("category").in((Object[]) categories));
        }

        if (paid != null) {
            predicates.add(paid ? cb.isTrue(root.get("paid")) : cb.isFalse(root.get("paid")));
        }

        if (rangeStart == null && rangeEnd == null) {
            predicates.add(cb.greaterThan(root.get("eventDate"), Util.now()));
        }

        if (rangeStart != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }

        if (rangeEnd != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }

        if (onlyAvailable != null && onlyAvailable) {
            predicates.add(cb.or(
                    cb.lessThan(root.get("confirmedRequests"), root.get("participantLimit")),
                    cb.equal(root.get("participantLimit"), 0)
            ));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(QueryUtils.toOrders(page.getSort(), root, cb));

        List<Event> events = entityManager.createQuery(cq)
                .setMaxResults(page.getPageSize())
                .getResultList();
        return events;
    }
}
