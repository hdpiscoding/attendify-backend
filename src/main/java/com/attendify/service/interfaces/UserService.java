package com.attendify.service.interfaces;

import com.attendify.dto.PaginatedResponseDTO;
import com.attendify.dto.UserDTO;
import com.attendify.entity.User;

import java.util.UUID;

public interface UserService {
    PaginatedResponseDTO<UserDTO> getAllUsers(int page, int limit);
    UserDTO getUserById(UUID id);
}
