package org.dev_projects.blog_api.repositories;

import org.dev_projects.blog_api.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
