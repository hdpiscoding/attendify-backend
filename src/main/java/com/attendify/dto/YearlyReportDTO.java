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
public class YearlyReportDTO {
    private int month;
    private int year;
    private UserDTO user;
    private int totalWorkingDays;
    private int totalAbsentDays;
    private LocalDateTime averageCheckInTime;
    private double totalHours;
    private int totalRequests;
    private int approvedRequests;
    private int rejectedRequests;
    private LocalDateTime createdAt;
}
