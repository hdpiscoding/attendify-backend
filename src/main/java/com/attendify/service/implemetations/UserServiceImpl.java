package com.attendify.service.implemetations;

import com.attendify.dto.PaginatedResponseDTO;
import com.attendify.dto.UserDTO;
import com.attendify.entity.User;
import com.attendify.mapper.UserMapper;
import com.attendify.repository.UserRepository;
import com.attendify.service.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PaginatedResponseDTO<UserDTO> getAllUsers(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        Page<User> users = userRepository.findAll(pageRequest);
        List<UserDTO> userDTOs = userMapper.toDtoList(users.getContent());
        return new PaginatedResponseDTO<>(
                userDTOs,
                users.getNumber() + 1,
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isLast()
        );
    }

    @Override
    public UserDTO getUserById(UUID id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id)));
    }

    @Override
    public void changePassword(UUID id, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found!"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
