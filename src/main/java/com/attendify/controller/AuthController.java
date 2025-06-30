package com.attendify.controller;

import com.attendify.base.BaseController;
import com.attendify.dto.AuthResponseDTO;
import com.attendify.dto.LoginDTO;
import com.attendify.dto.RegisterDTO;
import com.attendify.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterDTO request) {
        AuthResponseDTO response = authService.register(request);
        return buildResponse(response, HttpStatus.CREATED, "User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO request) {
        AuthResponseDTO response = authService.login(request);
        return buildResponse(response, HttpStatus.OK, "User logged in successfully");
    }

    @GetMapping("/callback/google")
    public ResponseEntity<Object> googleCallback(@AuthenticationPrincipal OidcUser user) {
        AuthResponseDTO response = authService.GoogleLogin(user);
        return buildResponse(response, HttpStatus.OK, "User logged in successfully via Google");
    }
}