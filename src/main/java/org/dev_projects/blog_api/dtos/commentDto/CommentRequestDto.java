package org.dev_projects.blog_api.dtos.commentDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    @NotBlank(message = "Comment content is required")
    @Size(min = 3, max = 100, message = "Comment content name must be between 3 and 100 characters")
    private String content;

    private Long postId;

}
