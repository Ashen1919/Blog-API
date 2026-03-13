package org.dev_projects.blog_api.dtos.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "User name is required")
    @Size(min = 3, max = 10, message = "User name must be between 3 and 10 characters")
    @Pattern(
            regexp = "^[A-Z].{2,10}$",
            message = "First character should be uppercase & only words are allowed."
    )
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must contain uppercase, digit, and special character(@, #, $, %, ^)"
    )
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter valid email")
    private String email;

}
