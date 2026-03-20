package org.dev_projects.blog_api.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dev_projects.blog_api.dtos.categoryDto.CategoryRequestDto;
import org.dev_projects.blog_api.dtos.categoryDto.CategoryResponseDto;
import org.dev_projects.blog_api.entities.Category;
import org.dev_projects.blog_api.exceptions.ResourceNotFoundException;
import org.dev_projects.blog_api.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    // Get all categories
    @Cacheable(value = "categories", key = "'all'")
    public List<CategoryResponseDto>  getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CategoryResponseDto.class))
                .collect(Collectors.toList());
    }

    // Create category
    @CacheEvict(value = "categories" , key = "'all'")
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = modelMapper.map(categoryRequestDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryResponseDto.class);
    }

    // Update a category
    @CacheEvict(value = "categories" , allEntries = true)
    public CategoryResponseDto updateCategory(int id, CategoryRequestDto categoryRequestDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
        existingCategory.setName(categoryRequestDto.getName());
        Category savedCategory = categoryRepository.save(existingCategory);
        return modelMapper.map(savedCategory, CategoryResponseDto.class);
    }

    // Delete a category
    @CacheEvict(value = "categories" , allEntries = true)
    public void deleteCategory(int id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
        categoryRepository.delete(existingCategory);
    }
}
