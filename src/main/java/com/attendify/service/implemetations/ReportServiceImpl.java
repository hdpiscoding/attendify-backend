package com.attendify.service.implemetations;

import com.attendify.dto.MonthlyReportDTO;
import com.attendify.dto.PaginatedResponseDTO;
import com.attendify.dto.YearlyReportDTO;
import com.attendify.entity.AttendanceLog;
import com.attendify.entity.Request;
import com.attendify.entity.User;
import com.attendify.mapper.UserMapper;
import com.attendify.repository.AttendanceLogRepository;
import com.attendify.repository.RequestRepository;
import com.attendify.repository.UserRepository;
import com.attendify.service.interfaces.ReportService;
import com.attendify.utils.enums.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.OptionalDouble;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final AttendanceLogRepository attendanceLogRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public List<MonthlyReportDTO> getMonthlyReport(UUID userId, int month, int year) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get attendance logs for the specified month and year
        List<AttendanceLog> attendanceLogs = attendanceLogRepository.findByUserIdAndMonthAndYear(userId, month, year);

        // Get all requests for the specified month
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();
        List<Request> requests = requestRepository.findByUserIdAndFromDateBetween(userId, startDate, endDate);

        // Count total, approved, and rejected requests
        int totalRequests = requests.size();
        int approvedRequests = (int) requests.stream()
                .filter(request -> request.getStatus() == RequestStatus.APPROVED)
                .count();
        int rejectedRequests = (int) requests.stream()
                .filter(request -> request.getStatus() == RequestStatus.REJECTED)
                .count();

        // Create a DTO for each attendance log
        return attendanceLogs.stream()
                .map(log -> MonthlyReportDTO.builder()
                        .user(userMapper.toDto(user))
                        .date(log.getWorkDate())
                        .checkInTime(log.getCheckIn())
                        .checkOutTime(log.getCheckOut())
                        .checkInStatus(log.getStatusIn())
                        .checkOutStatus(log.getStatusOut())
                        .totalHours(log.getTotalHours())
                        .totalRequests(totalRequests)
                        .approvedRequests(approvedRequests)
                        .rejectedRequests(rejectedRequests)
                        .createdAt(LocalDateTime.now())
                        .build())
                .toList();
    }

    @Override
    public List<YearlyReportDTO> getYearlyReport(UUID userId, int year) {
        return List.of();
    }
}
