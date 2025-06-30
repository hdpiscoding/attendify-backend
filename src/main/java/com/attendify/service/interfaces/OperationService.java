package com.attendify.service.interfaces;

import com.attendify.dto.*;

import java.util.UUID;

public interface OperationService {
    OperationResponseDTO checkIn(UUID userId, OperationRequestDTO operationRequestDTO);
    OperationResponseDTO checkOut(UUID userId, OperationRequestDTO operationRequestDTO);
    OperationStatusDTO getUserOperationStatusNow(UUID userId);
    PaginatedResponseDTO<OperationResponseDTO> getMyAttendanceLogs(UUID userId, int page, int limit);
}
