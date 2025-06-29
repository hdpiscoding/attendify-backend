package com.attendify.service.implemetations;

import com.attendify.dto.MonthlyReportDTO;
import com.attendify.dto.PaginatedResponseDTO;
import com.attendify.dto.YearlyReportDTO;
import com.attendify.service.interfaces.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    @Override
    public MonthlyReportDTO getMonthlyReport(UUID userId, int month, int year) {
        return null;
    }

    @Override
    public YearlyReportDTO getYearlyReport(UUID userId, int year) {
        return null;
    }

    @Override
    public PaginatedResponseDTO<YearlyReportDTO> getGeneralYearlyReport(int page, int limit, int year) {
        return null;
    }

    @Override
    public PaginatedResponseDTO<YearlyReportDTO> getGeneralMonthlyReport(int page, int limit, int month, int year) {
        return null;
    }
}
