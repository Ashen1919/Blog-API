package org.dev_projects.blog_api.repositories;

import org.dev_projects.blog_api.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
