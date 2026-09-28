package org.example.userpost.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userpost.api.dto.PostDto;
import org.example.userpost.api.dto.UserDto;
import org.example.userpost.api.dto.UserWithPostsDto;
import org.example.userpost.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @GetMapping("/usersWithPost")
    public ResponseEntity<List<UserWithPostsDto>> getAllUsersWithPosts() {
        return ResponseEntity.ok(userService.getAllUsersWithPosts());
    }

    @GetMapping("/userWithPost/{id}")
    public ResponseEntity<UserWithPostsDto> getUserWithPosts(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserWithPosts(id));
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<PostDto>> getPostsOfUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getPostsOfUser(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}