package com.attendify.controller;

import com.attendify.base.BaseController;
import com.attendify.dto.RequestDTO;
import com.attendify.dto.RequestFilterDTO;
import com.attendify.service.interfaces.JwtService;
import com.attendify.service.interfaces.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class RequestController extends BaseController {
    private final RequestService requestService;
    private final JwtService jwtService;

    @PostMapping("/filter")
    public ResponseEntity<Object> getRequests(@RequestBody RequestFilterDTO filter) {
        return buildResponse(requestService.getRequests(filter), HttpStatus.OK, "Get requests successfully!");
    }

    @PostMapping("")
    public ResponseEntity<Object> createRequest(@RequestHeader("Authorization") String authHeader, @RequestBody RequestDTO body) {
        UUID userId = jwtService.extractUserId(authHeader.substring(7));
        return buildResponse(requestService.createRequest(userId, body), HttpStatus.CREATED, "Create request successfully!");
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getMyRequests(@RequestHeader("Authorization") String authHeader) {
        UUID userId = jwtService.extractUserId(authHeader.substring(7));
        return buildResponse(requestService.getRequest(userId), HttpStatus.OK, "Get my requests successfully!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<Object> approveRequest(@PathVariable UUID id) {
        requestService.approveRequest(id);
        return buildResponse(null, HttpStatus.OK, "Approve request successfully!");
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> rejectRequest(@PathVariable UUID id) {
        requestService.rejectRequest(id);
        return buildResponse(null, HttpStatus.OK, "Reject request successfully!");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> deleteRequest(@PathVariable UUID id) {
        requestService.deleteRequest(id);
        return buildResponse(null, HttpStatus.OK, "Delete request successfully!");
    }
}
