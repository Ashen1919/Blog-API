package org.dev_projects.blog_api.repositories;

import org.dev_projects.blog_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
