package com.attendify.controller;

import com.attendify.base.BaseController;
import com.attendify.service.interfaces.JwtService;
import com.attendify.service.interfaces.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController extends BaseController {
    private final ReportService reportService;
    private final JwtService jwtService;

    @GetMapping("/me/monthly")
    public ResponseEntity<Object> getMonthlyReport(@RequestHeader("Authorization") String authHeader, @RequestParam Integer month , @RequestParam Integer year) {
        UUID userId = jwtService.extractUserId(authHeader.substring(7));
        return buildResponse(reportService.getMonthlyReport(userId, month, year), HttpStatus.OK,
                             "Get monthly report successfully!");
    }
}
