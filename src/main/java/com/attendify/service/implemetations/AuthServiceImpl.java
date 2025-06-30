package com.attendify.service.implemetations;

import com.attendify.dto.AuthResponseDTO;
import com.attendify.dto.LoginDTO;
import com.attendify.dto.RegisterDTO;
import com.attendify.dto.UserDTO;
import com.attendify.entity.User;
import com.attendify.exception.UserNotActiveException;
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
    public AuthResponseDTO GoogleLogin(OidcUser oidcUser) {
        String email = oidcUser.getEmail();
        String fullname = oidcUser.getFullName();
        String picture = oidcUser.getPicture();

        User user;

        if (userRepository.existsByEmail(email)) {
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.getFullname() == null || user.getFullname().isEmpty()) {
                user.setFullname(fullname);
            }

            if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
                user.setAvatar(picture);
            }

            user = userRepository.save(user);
        } else {
            user = User.builder()
                    .email(email)
                    .fullname(fullname)
                    .avatar(picture)
                    .active(true)
                    .role(Role.EMPLOYEE)
                    .department("Unassigned")
                    .build();

            user = userRepository.save(user);
        }

        String token = jwtService.generateToken(user);
        return AuthResponseDTO.builder()
                .token(token)
                .user(userMapper.toDto(user))
                .build();
    }
}