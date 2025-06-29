package com.attendify.service.implemetations;

import com.attendify.dto.OperationDTO;
import com.attendify.dto.OperationStatusDTO;
import com.attendify.entity.AttendanceLog;
import com.attendify.entity.User;
import com.attendify.repository.AttendanceLogRepository;
import com.attendify.repository.UserRepository;
import com.attendify.service.interfaces.GeofencingService;
import com.attendify.service.interfaces.OperationService;
import com.attendify.utils.constants.OperationConstants;
import com.attendify.utils.enums.CheckInStatus;
import com.attendify.utils.enums.CheckOutStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {
    private final AttendanceLogRepository attendanceLogRepository;
    private final GeofencingService geofencingService;
    private final UserRepository userRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static double calculateTotalHours(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return duration.toMinutes() / 60.0;
    }

    @Override
    @Transactional
    public void checkIn(UUID userId, OperationDTO operationDTO) {
        LocalTime startTime = LocalTime.parse(OperationConstants.START_TIME, formatter);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        if (geofencingService.isWithinGeofence(Double.parseDouble(operationDTO.getLatitude()), Double.parseDouble(operationDTO.getLongitude()))) {
            AttendanceLog attendanceLog = new AttendanceLog();
            attendanceLog.setUser(user);
            LocalDateTime now = LocalDateTime.now();
            attendanceLog.setCheckIn(now);
            attendanceLog.setWorkDate(now.toLocalDate());
            if (now.toLocalTime().isAfter(startTime)) {
                attendanceLog.setStatusIn(CheckInStatus.LATE);
            } else {
                attendanceLog.setStatusIn(CheckInStatus.ONTIME);
            }
            attendanceLogRepository.save(attendanceLog);
        } else {
            throw new IllegalArgumentException("User is not within the allowed geofence area for check-in.");
        }
    }

    @Override
    @Transactional
    public void checkOut(UUID userId, OperationDTO operationDTO) {
        LocalTime endTime = LocalTime.parse(OperationConstants.END_TIME, formatter);
        AttendanceLog attendanceLog = attendanceLogRepository.findByUserIdAndWorkDate(userId, LocalDate.now())
                .orElseThrow(() -> new IllegalArgumentException("No check-in record found for today."));
        if (attendanceLog.getCheckOut() != null) {
            throw new IllegalArgumentException("User has already checked out for today.");
        }
        if (geofencingService.isWithinGeofence(Double.parseDouble(operationDTO.getLatitude()), Double.parseDouble(operationDTO.getLongitude()))) {
            LocalDateTime now = LocalDateTime.now();
            attendanceLog.setCheckOut(now);
            if (now.toLocalTime().isBefore(endTime)) {
                attendanceLog.setStatusOut(CheckOutStatus.EARLY);
            } else {
                attendanceLog.setStatusOut(CheckOutStatus.ONTIME);
            }
            attendanceLog.setTotalHours(calculateTotalHours(attendanceLog.getCheckIn(), now));
            attendanceLogRepository.save(attendanceLog);
        } else {
            throw new IllegalArgumentException("User is not within the allowed geofence area for check-out.");
        }
    }

    @Override
    public OperationStatusDTO getUserOperationStatusNow(UUID userId) {
        AttendanceLog attendanceLog = attendanceLogRepository.findByUserIdAndWorkDate(userId, LocalDate.now())
                .orElse(null);
        if (attendanceLog == null) {
            return new OperationStatusDTO(false, false);
        }
        else {
            boolean isCheckedIn = attendanceLog.getCheckIn() != null;
            boolean isCheckedOut = attendanceLog.getCheckOut() != null;
            return new OperationStatusDTO(isCheckedIn, isCheckedOut);
        }
    }
}
