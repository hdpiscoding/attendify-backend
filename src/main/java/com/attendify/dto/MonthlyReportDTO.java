package com.attendify.dto;

import com.attendify.utils.enums.CheckInStatus;
import com.attendify.utils.enums.CheckOutStatus;
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
public class MonthlyReportDTO {
    private UserDTO user;
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private CheckInStatus checkInStatus;
    private CheckOutStatus checkOutStatus;
    private double totalHours;
    private int totalRequests;
    private int approvedRequests;
    private int rejectedRequests;
    private LocalDateTime createdAt;
}
