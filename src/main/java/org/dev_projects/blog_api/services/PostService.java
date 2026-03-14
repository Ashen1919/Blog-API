package org.dev_projects.blog_api.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dev_projects.blog_api.dtos.PageResponseDto;
import org.dev_projects.blog_api.dtos.postDto.PostRequestDto;
import org.dev_projects.blog_api.dtos.postDto.PostResponseDto;
import org.dev_projects.blog_api.entities.Category;
import org.dev_projects.blog_api.entities.Post;
import org.dev_projects.blog_api.entities.Tag;
import org.dev_projects.blog_api.entities.User;
import org.dev_projects.blog_api.exceptions.ResourceNotFoundException;
import org.dev_projects.blog_api.repositories.CategoryRepository;
import org.dev_projects.blog_api.repositories.PostRepository;
import org.dev_projects.blog_api.repositories.TagRepository;
import org.dev_projects.blog_api.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    // Create a post
    public PostResponseDto createPost(PostRequestDto postRequestDto, Principal principal) {
        Integer userId = Integer.valueOf(principal.getName());
        User author = userRepository.findById(userId).orElseThrow(() ->
                new UsernameNotFoundException("User not found with id " + userId));

        Category category = categoryRepository.findById(postRequestDto.getCategoryId()).orElseThrow(() ->
                new RuntimeException("Category not found with id " + postRequestDto.getCategoryId()));

        List<Tag> tags = tagRepository.findAllById(postRequestDto.getTagIds());

        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .category(category)
                .tags(tags)
                .author(author)
                .build();

        return modelMapper.map(postRepository.save(post), PostResponseDto.class);
    }

    // Get all posts
    public PageResponseDto<PostResponseDto> getAllPosts(int page, int size) {
        Pageable pageable =  PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Post> postPage = postRepository.findAll(pageable);

        List<PostResponseDto> content = postPage.getContent()
                .stream()
                .map(c -> modelMapper.map(c, PostResponseDto.class))
                .toList();

        return new PageResponseDto<>(
                content,
                postPage.getNumber(),
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.isLast()
        );
    }

    // Get post by ID
    public PostResponseDto getPostById(Long id) {
        Post existingPost =  postRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Post not found with id " + id));

        return modelMapper.map(existingPost, PostResponseDto.class);
    }

    // Get post By user id
    public PageResponseDto<PostResponseDto> getPostByAuthor(int page, int size, Principal principal) {
        Integer userId = Integer.valueOf(principal.getName());
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Post> postPage = postRepository.findByAuthor(author, pageable);

        List<PostResponseDto> content = postPage.getContent()
                .stream()
                .map(c -> modelMapper.map(c, PostResponseDto.class))
                .toList();

        return new PageResponseDto<>(
                content,
                postPage.getNumber(),
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.isLast()
        );
    }
}
