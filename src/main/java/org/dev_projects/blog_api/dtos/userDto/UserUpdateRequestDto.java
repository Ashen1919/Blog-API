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
public class UserUpdateRequestDto {
    @Size(min = 3, max = 10, message = "User name must be between 3 and 10 characters")
    @Pattern(
            regexp = "^[A-Z].{2,10}$",
            message = "First character should be uppercase & only words are allowed."
    )
    private String username;

}
