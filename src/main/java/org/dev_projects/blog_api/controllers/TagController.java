package org.dev_projects.blog_api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dev_projects.blog_api.dtos.PageResponseDto;
import org.dev_projects.blog_api.dtos.tagDto.TagRequestDto;
import org.dev_projects.blog_api.dtos.tagDto.TagResponseDto;
import org.dev_projects.blog_api.services.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<PageResponseDto<TagResponseDto>> getAllTags(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(tagService.getAllTags(page, size));
    }

    @PostMapping
    public ResponseEntity<TagResponseDto> createTag(@Valid @RequestBody TagRequestDto tagRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.createTag(tagRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponseDto> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagRequestDto tagRequestDto
    ) {
        return ResponseEntity.ok(tagService.updateTag(id, tagRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok(Map.of("message", "Tag deleted successfully"));
    }
}
