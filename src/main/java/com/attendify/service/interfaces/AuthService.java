package com.attendify.service.interfaces;

import com.attendify.dto.*;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface AuthService {
    AuthResponseDTO register(RegisterDTO request);
    AuthResponseDTO login(LoginDTO request);
    AuthResponseDTO GoogleLogin(GoogleLoginDTO user);
}
