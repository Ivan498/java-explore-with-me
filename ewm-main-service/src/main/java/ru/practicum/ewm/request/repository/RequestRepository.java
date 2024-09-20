package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.ewm.request.model.IRequestCount;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RequestRepository extends CrudRepository<Request, Long> {

    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    Long countByStatusAndEventId(RequestStatus status, Long eventId);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByStatusAndIdIn(RequestStatus status, Collection<Long> requestIds);

    @Query("SELECT r.event.id AS eventRequests, COUNT(DISTINCT r.id) AS countRequests " +
            "FROM Request AS r WHERE r.event.id  IN (:eventIds) AND r.status = :status " +
            "GROUP BY r.event.id ")
    List<IRequestCount> countAllByStatusAndEventIdIn(RequestStatus status, Set<Long> eventIds);

    List<Request> findAllByIdIn(List<Long> requestIds);
}
