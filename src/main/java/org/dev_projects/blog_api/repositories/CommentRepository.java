package org.dev_projects.blog_api.repositories;

import org.dev_projects.blog_api.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
