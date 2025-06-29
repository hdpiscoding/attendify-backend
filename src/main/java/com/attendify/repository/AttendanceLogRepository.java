package com.attendify.repository;

import com.attendify.dto.PaginatedResponseDTO;
import com.attendify.entity.AttendanceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, UUID> {
    Optional<AttendanceLog> findByUserIdAndWorkDate(UUID userId, LocalDate workDate);
    Page<AttendanceLog> findByUserId(UUID userId, Pageable pageable);
    @Query("SELECT a FROM AttendanceLog a WHERE a.user.id = :userId AND " +
            "YEAR(a.workDate) = :year AND MONTH(a.workDate) = :month")
    List<AttendanceLog> findByUserIdAndMonthAndYear(
            @Param("userId") UUID userId,
            @Param("month") int month,
            @Param("year") int year);
}
