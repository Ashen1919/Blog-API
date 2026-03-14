package org.dev_projects.blog_api.dtos.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserResponseDto {
    private int id;
    private String username;
    private LocalDateTime updated_at;
}
