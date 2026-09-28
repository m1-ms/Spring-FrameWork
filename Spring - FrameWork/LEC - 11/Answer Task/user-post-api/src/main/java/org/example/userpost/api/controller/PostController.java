package org.example.userpost.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userpost.api.dto.PostDto;
import org.example.userpost.api.dto.PostWithUserDto;
import org.example.userpost.api.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(dto));
    }

    @GetMapping("/postsWithUsers")
    public ResponseEntity<List<PostWithUserDto>> getAllPostsWithUsers() {
        return ResponseEntity.ok(postService.getAllPostsWithUsers());
    }

    @GetMapping("/postWithUsers/{id}")
    public ResponseEntity<PostWithUserDto> getPostWithUser(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostWithUser(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @Valid @RequestBody PostDto dto) {
        return ResponseEntity.ok(postService.updatePost(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}