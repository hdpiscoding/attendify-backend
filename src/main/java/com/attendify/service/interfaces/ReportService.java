package com.attendify.service.interfaces;

import com.attendify.dto.MonthlyReportDTO;
import com.attendify.dto.PaginatedResponseDTO;
import com.attendify.dto.YearlyReportDTO;

import java.util.List;
import java.util.UUID;

public interface ReportService {
    List<MonthlyReportDTO> getMonthlyReport(UUID userId, int month, int year);
    List<YearlyReportDTO> getYearlyReport(UUID userId, int year);
}
