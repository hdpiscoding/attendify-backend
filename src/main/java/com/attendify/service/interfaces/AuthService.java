package com.attendify.service.interfaces;

import com.attendify.dto.AuthResponseDTO;
import com.attendify.dto.LoginDTO;
import com.attendify.dto.RegisterDTO;
import com.attendify.dto.UserDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterDTO request);
    AuthResponseDTO login(LoginDTO request);
}
