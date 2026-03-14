package org.dev_projects.blog_api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dev_projects.blog_api.dtos.userDto.*;
import org.dev_projects.blog_api.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserControllers {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequestDto));
    }

    @PutMapping
    public ResponseEntity<UpdateUserResponseDto> updateUser(
            @Valid
            @RequestBody UserUpdateRequestDto  userUpdateRequestDto,
            Principal principal
    ) {
        return ResponseEntity.ok(userService.updateUser(userUpdateRequestDto, principal));
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @Valid
            @RequestBody UpdatePasswordDto dto,
            Principal principal
    ) {
        userService.updatePassword(dto.getOldPassword(),  dto.getNewPassword(), principal);
        return ResponseEntity.ok().build();
    }
}
