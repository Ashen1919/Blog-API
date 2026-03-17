package org.dev_projects.blog_api.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dev_projects.blog_api.dtos.PageResponseDto;
import org.dev_projects.blog_api.dtos.postDto.PostRequestDto;
import org.dev_projects.blog_api.dtos.postDto.PostResponseDto;
import org.dev_projects.blog_api.dtos.postDto.UpdatePostRequestDto;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.time.LocalDateTime;
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
    @CacheEvict(value = {"posts_all", "posts_by_id", "posts_by_user"}, allEntries = true)
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
    @Cacheable(value = "posts_all", key = "#page + '_' + #size")
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
    @Cacheable(value = "posts_by_id", key = "#id")
    public PostResponseDto getPostById(Long id) {
        Post existingPost =  postRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Post not found with id " + id));

        return modelMapper.map(existingPost, PostResponseDto.class);
    }

    // Get post By author
    @Cacheable(value = "posts_by_user", key = "#principal.name + '_' + #page + '_' + #size")
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

    // Update posts
    @CacheEvict(value = {"posts_all", "posts_by_id", "posts_by_user"}, allEntries = true)
    public PostResponseDto updatePost(Long id ,UpdatePostRequestDto updatePostRequestDto) {
        // Check post is exist
        Post existingPost = postRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Post not found with id " + id));

        // Check category is existed
        if(updatePostRequestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(updatePostRequestDto.getCategoryId()).orElseThrow(() ->
                    new RuntimeException("Category not found with id " + updatePostRequestDto.getCategoryId()));

            existingPost.setCategory(category);
        }

        // Check title is exist
        if(updatePostRequestDto.getTitle() != null) {
            existingPost.setTitle(updatePostRequestDto.getTitle());
        }

        // Check content is exist
        if(updatePostRequestDto.getContent() != null) {
            existingPost.setContent(updatePostRequestDto.getContent());
        }

        // Check tags are exist
        if(updatePostRequestDto.getTagIds() != null && !updatePostRequestDto.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(updatePostRequestDto.getTagIds());
            existingPost.setTags(tags);
        }

        existingPost.setUpdatedAt(LocalDateTime.now());

        return modelMapper.map(postRepository.save(existingPost), PostResponseDto.class);
    }

    // Delete post
    @CacheEvict(value = {"posts_all", "posts_by_id", "posts_by_user"}, allEntries = true)
    public void deletePost(Long id ,Principal principal) throws AccessDeniedException {
        int userId = Integer.parseInt(principal.getName());

        Post existingPost = postRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Post not found with id " + id));

        // Check post belongs to the requested user
        if(existingPost.getAuthor().getId() != userId) {
            throw new AccessDeniedException("You are not authorized to delete this post");
        }

        postRepository.delete(existingPost);
    }
}
