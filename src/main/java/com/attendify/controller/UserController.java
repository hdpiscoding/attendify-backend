package com.attendify.controller;

import com.attendify.base.BaseController;
import com.attendify.service.interfaces.JwtService;
import com.attendify.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<Object> getMe(@RequestHeader("Authorization") String authHeader){
        UUID userId = jwtService.extractUserId(authHeader.substring(7));
        return buildResponse(userService.getUserById(userId), HttpStatus.OK, "User details fetched successfully");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("")
    public ResponseEntity<Object> getAllUsers(@RequestParam Integer page , @RequestParam Integer limit) {
        return buildResponse(userService.getAllUsers(page, limit), HttpStatus.OK, "All users fetched successfully");
    }
}
