package org.dev_projects.blog_api.dtos.commentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dev_projects.blog_api.dtos.postDto.UserSummaryDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserSummaryDto author;
    private PostSummaryDto postId;
}
