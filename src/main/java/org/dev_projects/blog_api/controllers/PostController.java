package org.dev_projects.blog_api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dev_projects.blog_api.dtos.PageResponseDto;
import org.dev_projects.blog_api.dtos.postDto.PostRequestDto;
import org.dev_projects.blog_api.dtos.postDto.PostResponseDto;
import org.dev_projects.blog_api.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @Valid
            @RequestBody PostRequestDto postRequestDto,
            Principal principal
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postRequestDto, principal));
    }

    @GetMapping("/allPosts")
    public ResponseEntity<PageResponseDto<PostResponseDto>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(postService.getAllPosts(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/my-posts")
    public ResponseEntity<PageResponseDto<PostResponseDto>> getMyPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal
    ) {
        return ResponseEntity.ok(postService.getPostByAuthor(page, size, principal));
    }
}
