package com.attendify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationResponseDTO {
    private String operation;
    private LocalDate date;
    private LocalDateTime time;
    private String status;
}
