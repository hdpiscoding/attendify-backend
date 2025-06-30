package com.attendify.service.implemetations;

import com.attendify.dto.MonthlyReportDTO;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final AttendanceLogRepository attendanceLogRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private LocalDateTime calculateAverageTime(List<AttendanceLog> logs,
                                               java.util.function.Function<AttendanceLog, LocalDateTime> timeExtractor) {
        if (logs.isEmpty()) {
            return null;
        }

        OptionalDouble avgHour = logs.stream()
                .map(timeExtractor)
                .filter(Objects::nonNull)
                .mapToInt(LocalDateTime::getHour)
                .average();

        OptionalDouble avgMinute = logs.stream()
                .map(timeExtractor)
                .filter(Objects::nonNull)
                .mapToInt(LocalDateTime::getMinute)
                .average();

        if (avgHour.isPresent() && avgMinute.isPresent()) {
            return LocalDateTime.of(
                    LocalDate.now(),
                    LocalTime.of((int) avgHour.getAsDouble(), (int) avgMinute.getAsDouble())
            );
        }

        return null;
    }

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<YearlyReportDTO> yearlyReports = new ArrayList<>();

        // For each month in the year
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            // Get attendance logs for this month
            List<AttendanceLog> attendanceLogs = attendanceLogRepository.findByUserIdAndMonthAndYear(userId, month, year);

            // Get requests for this month
            List<Request> requests = requestRepository.findByUserIdAndFromDateBetween(userId, startDate, endDate);

            // Calculate statistics
            int totalWorkingDays = attendanceLogs.size();
            double totalHours = attendanceLogs.stream()
                    .mapToDouble(AttendanceLog::getTotalHours)
                    .sum();

            // Calculate average check-in and check-out times
            LocalDateTime averageCheckInTime = calculateAverageTime(attendanceLogs,
                    AttendanceLog::getCheckIn);
            LocalDateTime averageCheckOutTime = calculateAverageTime(attendanceLogs,
                    AttendanceLog::getCheckOut);

            // Count request statistics
            int totalRequests = requests.size();
            int approvedRequests = (int) requests.stream()
                    .filter(r -> r.getStatus() == RequestStatus.APPROVED)
                    .count();
            int rejectedRequests = (int) requests.stream()
                    .filter(r -> r.getStatus() == RequestStatus.REJECTED)
                    .count();

            // Create and add monthly report to the list
            YearlyReportDTO monthlyReport = YearlyReportDTO.builder()
                    .month(month)
                    .year(year)
                    .user(userMapper.toDto(user))
                    .totalWorkingDays(totalWorkingDays)
                    .averageCheckInTime(averageCheckInTime)
                    .averageCheckOutTime(averageCheckOutTime)
                    .totalHours(totalHours)
                    .totalRequests(totalRequests)
                    .approvedRequests(approvedRequests)
                    .rejectedRequests(rejectedRequests)
                    .createdAt(LocalDateTime.now())
                    .build();

            yearlyReports.add(monthlyReport);
        }

        return yearlyReports;
    }
}
