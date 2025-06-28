package com.attendify.dto;

import com.attendify.utils.enums.RequestStatus;
import com.attendify.utils.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDTO {
    private UUID id;
    private UserDTO user;
    private RequestStatus status;
    private RequestType type;
    private String reason;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
