package org.dev_projects.blog_api.dtos.postDto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequestDto {
    @Size(min = 10, max = 100, message = "Title must be between 10 and 100 characters")
    private String title;

    @Size(min = 10, message = "Content must be at least 10 characters")
    private String content;

    private Integer categoryId;

    private List<Long> tagIds =  new ArrayList<>();
}
