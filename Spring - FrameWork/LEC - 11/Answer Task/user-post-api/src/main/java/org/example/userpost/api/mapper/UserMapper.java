package org.example.userpost.api.mapper;

import org.example.userpost.api.dto.UserDto;
import org.example.userpost.api.dto.PostDto;
import org.example.userpost.api.dto.UserWithPostsDto;
import org.example.userpost.api.entity.User;

import java.util.List;

public class UserMapper {

    public static User toEntity(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .age(dto.getAge())
                .password(dto.getPassword())
                .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .password(user.getPassword())
                .build();
    }

    public static UserWithPostsDto toDtoWithPosts(User user, List<PostDto> posts) {
        return UserWithPostsDto.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .posts(posts)
                .build();
    }
}