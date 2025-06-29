package com.attendify.repository;

import com.attendify.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID>, JpaSpecificationExecutor<Request> {
    List<Request> findByUserIdAndFromDateBetween(UUID userId, LocalDate startDate, LocalDate endDate);
}
