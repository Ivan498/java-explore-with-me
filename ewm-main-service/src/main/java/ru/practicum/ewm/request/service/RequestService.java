package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.model.Request;

import java.util.List;

public interface RequestService {

    Request addRequest(Long userId, Long eventId);

    List<Request> getAllUserRequests(Long userId);

    Request cancelRequestByUser(Long userId, Long requestId);

    List<Request> getAllEventRequestsByInitiator(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestsByInitiator(Long userId,
                                                             Long eventId,
                                                             EventRequestStatusUpdateRequest update);
}
