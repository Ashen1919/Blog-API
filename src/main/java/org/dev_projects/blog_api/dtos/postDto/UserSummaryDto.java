package org.dev_projects.blog_api.dtos.postDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDto {
    private int id;
    private String username;
    private String email;
}
