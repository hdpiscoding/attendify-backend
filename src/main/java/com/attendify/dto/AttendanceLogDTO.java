package com.attendify.dto;

import com.attendify.utils.enums.CheckInStatus;
import com.attendify.utils.enums.CheckOutStatus;
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
public class AttendanceLogDTO {
    private UUID id;
    private UserDTO user;
    private LocalDate workDate;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private CheckInStatus statusIn;
    private CheckOutStatus statusOut;
    private double totalHours;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
