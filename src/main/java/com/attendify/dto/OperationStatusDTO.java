package com.attendify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationStatusDTO {
    private Boolean isCheckIn;
    private Boolean isCheckOut;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}
