package org.dev_projects.blog_api.dtos.postDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dev_projects.blog_api.dtos.categoryDto.CategoryResponseDto;
import org.dev_projects.blog_api.dtos.tagDto.TagResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TagResponseDto> tags;
    private CategoryResponseDto category;
    private UserSummaryDto author;
}
