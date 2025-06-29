package com.attendify.repository;

import com.attendify.entity.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, UUID> {
    Optional<AttendanceLog> findByUserIdAndWorkDate(UUID userId, LocalDate workDate);

}
