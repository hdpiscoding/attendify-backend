package com.attendify.service.interfaces;

import com.attendify.dto.OperationDTO;
import com.attendify.dto.OperationStatusDTO;

import java.util.UUID;

public interface OperationService {
    void checkIn(UUID userId, OperationDTO operationDTO);
    void checkOut(UUID userId, OperationDTO operationDTO);
    OperationStatusDTO getUserOperationStatusNow(UUID userId);
}
