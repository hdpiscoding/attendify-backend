package com.attendify.repository;

import com.attendify.dto.PaginatedResponseDTO;
import com.attendify.entity.AttendanceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, UUID> {
    Optional<AttendanceLog> findByUserIdAndWorkDate(UUID userId, LocalDate workDate);
    Page<AttendanceLog> findByUserId(UUID userId, Pageable pageable);
}
