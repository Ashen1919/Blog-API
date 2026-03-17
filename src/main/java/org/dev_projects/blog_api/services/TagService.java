package org.dev_projects.blog_api.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dev_projects.blog_api.dtos.PageResponseDto;
import org.dev_projects.blog_api.dtos.tagDto.TagRequestDto;
import org.dev_projects.blog_api.dtos.tagDto.TagResponseDto;
import org.dev_projects.blog_api.entities.Tag;
import org.dev_projects.blog_api.exceptions.ResourceNotFoundException;
import org.dev_projects.blog_api.repositories.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    // Get all tags
    @Cacheable(value = "tags" , key = "#page + '_' + #size")
    public PageResponseDto<TagResponseDto> getAllTags(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Tag> pageTag = tagRepository.findAll(pageable);

        List<TagResponseDto> content = pageTag.getContent()
                .stream()
                .map(tag -> modelMapper.map(tag, TagResponseDto.class))
                .toList();

        return new PageResponseDto<>(
                content,
                pageTag.getNumber(),
                pageTag.getSize(),
                pageTag.getTotalElements(),
                pageTag.getTotalPages(),
                pageTag.isLast()
        );
    }

    // Create a tag
    @CacheEvict(value = "tags" , allEntries = true)
    public TagResponseDto createTag(TagRequestDto tagRequestDto) {
        Tag tag = modelMapper.map(tagRequestDto, Tag.class);
        Tag savedTag = tagRepository.save(tag);
        return modelMapper.map(savedTag, TagResponseDto.class);
    }

    // Update a tag
    @CacheEvict(value = "tags" , allEntries = true)
    public TagResponseDto updateTag(Long id ,TagRequestDto tagRequestDto) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id " + id));
        existingTag.setName(tagRequestDto.getName());
        Tag updatedTag = tagRepository.save(existingTag);
        return modelMapper.map(updatedTag, TagResponseDto.class);
    }

    // Delete tag
    @CacheEvict(value = "tags" , allEntries = true)
    public void deleteTag(Long id) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id " + id));
        tagRepository.delete(existingTag);
    }

}
