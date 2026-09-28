package org.example.userpost.api.service;

import lombok.RequiredArgsConstructor;
import org.example.userpost.api.dto.PostDto;
import org.example.userpost.api.dto.PostWithUserDto;
import org.example.userpost.api.entity.Post;
import org.example.userpost.api.entity.User;
import org.example.userpost.api.exception.ResourceNotFoundException;
import org.example.userpost.api.mapper.PostMapper;
import org.example.userpost.api.repository.PostRepository;
import org.example.userpost.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostDto createPost(PostDto dto) {
        User user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        }
        Post post = PostMapper.toEntity(dto, user);
        post.setId(null);
        return PostMapper.toDto(postRepository.save(post));
    }

    public PostDto getPostById(Long id) {
        return PostMapper.toDto(findPostOrThrow(id));
    }

    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream().map(PostMapper::toDto).toList();
    }

    public PostDto updatePost(Long id, PostDto dto) {
        Post post = findPostOrThrow(id);
        post.setText(dto.getText());
        post.setImagePath(dto.getImagePath());
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
            post.setUser(user);
        }
        return PostMapper.toDto(postRepository.save(post));
    }

    public void deletePost(Long id) {
        postRepository.delete(findPostOrThrow(id));
    }

    public PostWithUserDto getPostWithUser(Long id) {
        return PostMapper.toDtoWithUser(findPostOrThrow(id));
    }

    public List<PostWithUserDto> getAllPostsWithUsers() {
        return postRepository.findAll().stream().map(PostMapper::toDtoWithUser).toList();
    }

    private Post findPostOrThrow(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }
}