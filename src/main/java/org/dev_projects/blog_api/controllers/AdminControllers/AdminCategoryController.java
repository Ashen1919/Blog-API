package org.dev_projects.blog_api.controllers.AdminControllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dev_projects.blog_api.dtos.categoryDto.CategoryRequestDto;
import org.dev_projects.blog_api.dtos.categoryDto.CategoryResponseDto;
import org.dev_projects.blog_api.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable int id,
            @Valid @RequestBody CategoryRequestDto categoryRequestDto
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(Map.of("message", "Category deleted successfully"));
    }
}
