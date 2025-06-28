package com.attendify.service.interfaces;

import com.attendify.dto.PaginatedResponseDTO;
import com.attendify.dto.RequestDTO;
import com.attendify.dto.RequestFilterDTO;

import java.util.UUID;

public interface RequestService {
    PaginatedResponseDTO<RequestDTO> getRequests(RequestFilterDTO filter);
    RequestDTO getRequest(UUID id);
    RequestDTO createRequest(UUID userId, RequestDTO requestDTO);
    void approveRequest(UUID requestId);
    void rejectRequest(UUID requestId);
    void deleteRequest(UUID requestId);
}
