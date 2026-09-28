package org.example.userpost.api.mapper;

import org.example.userpost.api.dto.PostDto;
import org.example.userpost.api.dto.PostWithUserDto;
import org.example.userpost.api.entity.Post;
import org.example.userpost.api.entity.User;

public class PostMapper {

    public static Post toEntity(PostDto dto, User user) {
        return Post.builder()
                .id(dto.getId())
                .text(dto.getText())
                .imagePath(dto.getImagePath())
                .user(user)
                .build();
    }

    public static PostDto toDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .text(post.getText())
                .imagePath(post.getImagePath())
                .userId(post.getUser() != null ? post.getUser().getId() : null)
                .build();
    }

    public static PostWithUserDto toDtoWithUser(Post post) {
        return PostWithUserDto.builder()
                .id(post.getId())
                .text(post.getText())
                .imagePath(post.getImagePath())
                .user(post.getUser() != null ? UserMapper.toDto(post.getUser()) : null)
                .build();
    }
}