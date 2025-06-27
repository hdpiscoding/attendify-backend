package com.attendify.entity;

import com.attendify.utils.enums.CheckInStatus;
import com.attendify.utils.enums.CheckOutStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "attendance_logs", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "work_date"})
})
public class AttendanceLog {
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "check_in")
    private LocalDateTime checkIn;

    @Column(name = "check_out")
    private LocalDateTime checkOut;

    @Column(name = "status_in")
    @Enumerated(EnumType.STRING)
    private CheckInStatus statusIn;

    @Column(name = "status_out")
    @Enumerated(EnumType.STRING)
    private CheckOutStatus statusOut;

    @Column(name = "total_hours")
    private double totalHours = 0.0;
}
