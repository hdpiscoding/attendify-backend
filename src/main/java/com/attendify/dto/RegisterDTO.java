package com.attendify.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDTO {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Fullname is required")
    private String fullname;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Date of Birth is required")
    private LocalDate dob;

    @NotBlank(message = "Department is required")
    private String department;
}
