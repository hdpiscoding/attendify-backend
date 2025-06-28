package com.attendify.dto;

import com.attendify.utils.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private UUID id;
    private String email;
    private String fullname;
    private String phone;
    private String avatar;
    private Role role;
    private LocalDate dob;
    private String department;
}
