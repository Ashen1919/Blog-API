package org.dev_projects.blog_api.dtos.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private int id;
    private String username;
    private String email;
    private String role;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
