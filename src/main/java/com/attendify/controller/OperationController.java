package com.attendify.controller;

import com.attendify.base.BaseController;
import com.attendify.dto.OperationDTO;
import com.attendify.service.interfaces.JwtService;
import com.attendify.service.interfaces.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/operations")
@RequiredArgsConstructor
public class OperationController extends BaseController {
    private final OperationService operationService;
    private final JwtService jwtService;

    @PostMapping("/check-in")
    public ResponseEntity<Object> checkIn(@RequestHeader("Authorization") String authHeader,@RequestBody OperationDTO operationDTO) {
        UUID userId = jwtService.extractUserId(authHeader.substring(7));
        operationService.checkIn(userId, operationDTO);
        return buildResponse(null, HttpStatus.OK, "Check in successfully");
    }

    @PostMapping("/check-out")
    public ResponseEntity<Object> checkOut(@RequestHeader("Authorization") String authHeader,@RequestBody OperationDTO operationDTO) {
        UUID userId = jwtService.extractUserId(authHeader.substring(7));
        operationService.checkOut(userId, operationDTO);
        return buildResponse(null, HttpStatus.OK, "Check out successfully");
    }

    @GetMapping("status/me")
    public ResponseEntity<Object> getUserOperationStatusNow(@RequestHeader("Authorization") String authHeader) {
        UUID userId = jwtService.extractUserId(authHeader.substring(7));
        return buildResponse(operationService.getUserOperationStatusNow(userId), HttpStatus.OK, "User status retrieved successfully");
    }
}
