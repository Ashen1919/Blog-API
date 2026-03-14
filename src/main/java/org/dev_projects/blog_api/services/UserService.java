package org.dev_projects.blog_api.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dev_projects.blog_api.dtos.PageResponseDto;
import org.dev_projects.blog_api.dtos.userDto.UpdateUserResponseDto;
import org.dev_projects.blog_api.dtos.userDto.UserRequestDto;
import org.dev_projects.blog_api.dtos.userDto.UserResponseDto;
import org.dev_projects.blog_api.dtos.userDto.UserUpdateRequestDto;
import org.dev_projects.blog_api.entities.Role;
import org.dev_projects.blog_api.entities.User;
import org.dev_projects.blog_api.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    // Create a user
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = modelMapper.map(userRequestDto, User.class);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setRole(Role.user);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    // Get all users
    public PageResponseDto<UserResponseDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<User> users = userRepository.findAll(pageable);

        List<UserResponseDto> content =  users.getContent()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();

        return new PageResponseDto<>(
                content,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isLast()
        );
    }

    // Update users
    public UpdateUserResponseDto updateUser(UserUpdateRequestDto userUpdateRequestDto, Principal principal) {
        Integer userId = Integer.valueOf(principal.getName());
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        existingUser.setUsername(userUpdateRequestDto.getUsername());
        existingUser.setUpdated_at(LocalDateTime.now());

        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UpdateUserResponseDto.class);
    }

    // Update password
    public void updatePassword(String oldPassword, String newPassword, Principal principal) {
        Integer userId = Integer.valueOf(principal.getName());
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));
        existingUser.setUpdated_at(LocalDateTime.now());
        userRepository.save(existingUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
