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
import java.util.ArrayList;
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

    // Helper method to calculate average check-in time
    private LocalDateTime calculateAverageCheckInTime(List<AttendanceLog> logs) {
        if (logs.isEmpty()) {
            return null;
        }

        OptionalDouble avgHour = logs.stream()
                .filter(log -> log.getCheckIn() != null)
                .mapToInt(log -> log.getCheckIn().getHour())
                .average();

        OptionalDouble avgMinute = logs.stream()
                .filter(log -> log.getCheckIn() != null)
                .mapToInt(log -> log.getCheckIn().getMinute())
                .average();

        if (avgHour.isPresent() && avgMinute.isPresent()) {
            return LocalDateTime.of(
                    LocalDate.now(),
                    LocalTime.of((int) avgHour.getAsDouble(), (int) avgMinute.getAsDouble())
            );
        }

        return null;
    }

    // Helper method to calculate business days in month
    private int calculateBusinessDaysInMonth(int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int days = yearMonth.lengthOfMonth();
        int businessDays = 0;

        for (int day = 1; day <= days; day++) {
            LocalDate date = LocalDate.of(year, month, day);
            if (date.getDayOfWeek().getValue() < 6) { // Monday to Friday
                businessDays++;
            }
        }

        return businessDays;
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

            // Get attendance logs and requests for this month
            List<AttendanceLog> attendanceLogs = attendanceLogRepository.findByUserIdAndMonthAndYear(userId, month, year);
            List<Request> requests = requestRepository.findByUserIdAndFromDateBetween(userId, startDate, endDate);

            // Calculate statistics
            int totalWorkingDays = attendanceLogs.size();
            int businessDays = calculateBusinessDaysInMonth(month, year);
            int totalAbsentDays = businessDays - totalWorkingDays;

            // Calculate average check-in time
            LocalDateTime averageCheckInTime = calculateAverageCheckInTime(attendanceLogs);

            // Calculate total hours
            double totalHours = attendanceLogs.stream()
                    .mapToDouble(AttendanceLog::getTotalHours)
                    .sum();

            // Count requests
            int totalRequests = requests.size();
            int approvedRequests = (int) requests.stream()
                    .filter(r -> r.getStatus() == RequestStatus.APPROVED)
                    .count();
            int rejectedRequests = (int) requests.stream()
                    .filter(r -> r.getStatus() == RequestStatus.REJECTED)
                    .count();

            // Build and add the monthly report to our yearly list
            YearlyReportDTO report = YearlyReportDTO.builder()
                    .month(month)
                    .year(year)
                    .user(userMapper.toDto(user))
                    .totalWorkingDays(totalWorkingDays)
                    .totalAbsentDays(totalAbsentDays)
                    .averageCheckInTime(averageCheckInTime)
                    .totalHours(totalHours)
                    .totalRequests(totalRequests)
                    .approvedRequests(approvedRequests)
                    .rejectedRequests(rejectedRequests)
                    .createdAt(LocalDateTime.now())
                    .build();

            yearlyReports.add(report);
        }

        return yearlyReports;
    }
}
