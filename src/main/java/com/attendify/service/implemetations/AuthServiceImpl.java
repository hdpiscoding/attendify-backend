package com.attendify.service.implemetations;

import com.attendify.dto.AuthResponseDTO;
import com.attendify.dto.LoginDTO;
import com.attendify.dto.RegisterDTO;
import com.attendify.dto.UserDTO;
import com.attendify.entity.User;
import com.attendify.mapper.UserMapper;
import com.attendify.repository.UserRepository;
import com.attendify.service.interfaces.AuthService;
import com.attendify.service.interfaces.JwtService;
import com.attendify.utils.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO register(RegisterDTO request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
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
    public AuthResponseDTO login(LoginDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtService.generateToken(user);
        UserDTO userDTO = userMapper.toDto(user);
        return AuthResponseDTO
                .builder()
                .user(userDTO)
                .token(token)
                .build();
    }
}