package com.attendify.service.interfaces;

import com.attendify.dto.MonthlyReportDTO;
import com.attendify.dto.PaginatedResponseDTO;
import com.attendify.dto.YearlyReportDTO;

import java.util.UUID;

public interface ReportService {
    MonthlyReportDTO getMonthlyReport(UUID userId, int month, int year);
    YearlyReportDTO getYearlyReport(UUID userId, int year);
    PaginatedResponseDTO<YearlyReportDTO> getGeneralYearlyReport(int page, int limit, int year);
    PaginatedResponseDTO<YearlyReportDTO> getGeneralMonthlyReport(int page, int limit, int month, int year);
}
