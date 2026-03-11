package org.dev_projects.blog_api.repositories;

import org.dev_projects.blog_api.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
}
