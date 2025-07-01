package com.attendify.service.implemetations;

import com.attendify.dto.*;
import com.attendify.entity.User;
import com.attendify.exception.UserNotActiveException;
import com.attendify.exception.UserNotExistException;
import com.attendify.mapper.UserMapper;
import com.attendify.repository.UserRepository;
import com.attendify.service.interfaces.AuthService;
import com.attendify.service.interfaces.JwtService;
import com.attendify.utils.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterDTO request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode("123456"));
        user.setFullname(request.getFullname());
        user.setDob(request.getDob());
        user.setPhone(request.getPhone());
        user.setDepartment(request.getDepartment());
        user.setActive(true);
        user.setRole(Role.EMPLOYEE);
        User newUser = userRepository.save(user);
        String token = jwtService.generateToken(newUser);
        return AuthResponseDTO
                .builder()
                .user(userMapper.toDto(newUser))
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public AuthResponseDTO login(LoginDTO request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isActive()) {
            throw new UserNotActiveException("User is not active, please contact support.");
        }
        String token = jwtService.generateToken(user);
        UserDTO userDTO = userMapper.toDto(user);
        return AuthResponseDTO
                .builder()
                .user(userDTO)
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public AuthResponseDTO GoogleLogin(GoogleLoginDTO user) {
        User temp_user;
        if (userRepository.existsByEmail(user.getEmail())) {
            temp_user = userRepository.findByEmail(user.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (temp_user.getFullname() == null || temp_user.getFullname().isEmpty()) {
                temp_user.setFullname(user.getName());
            }
            if (temp_user.getAvatar() == null || temp_user.getAvatar().isEmpty()) {
                temp_user.setAvatar(user.getImage());
            }
            temp_user = userRepository.save(temp_user);
            String token = jwtService.generateToken(temp_user);
            return AuthResponseDTO.builder()
                    .token(token)
                    .user(userMapper.toDto(temp_user))
                    .build();
        }
        else {
            throw new UserNotExistException("User not exists, please contact support.");
        }
    }
}