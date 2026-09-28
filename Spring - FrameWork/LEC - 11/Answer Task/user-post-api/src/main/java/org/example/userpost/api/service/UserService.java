package org.example.userpost.api.service;

import lombok.RequiredArgsConstructor;
import org.example.userpost.api.dto.PostDto;
import org.example.userpost.api.dto.UserDto;
import org.example.userpost.api.dto.UserWithPostsDto;
import org.example.userpost.api.entity.User;
import org.example.userpost.api.exception.ResourceNotFoundException;
import org.example.userpost.api.mapper.PostMapper;
import org.example.userpost.api.mapper.UserMapper;
import org.example.userpost.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto createUser(UserDto dto) {
        User user = UserMapper.toEntity(dto);
        user.setId(null);
        return UserMapper.toDto(userRepository.save(user));
    }

    public UserDto getUserById(Long id) {
        return UserMapper.toDto(findUserOrThrow(id));
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDto).toList();
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = findUserOrThrow(id);
        user.setName(dto.getName());
        user.setAge(dto.getAge());
        user.setPassword(dto.getPassword());
        return UserMapper.toDto(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.delete(findUserOrThrow(id));
    }

    public List<PostDto> getPostsOfUser(Long id) {
        User user = findUserOrThrow(id);
        return user.getPosts().stream().map(PostMapper::toDto).toList();
    }

    public UserWithPostsDto getUserWithPosts(Long id) {
        User user = findUserOrThrow(id);
        List<PostDto> posts = user.getPosts().stream().map(PostMapper::toDto).toList();
        return UserMapper.toDtoWithPosts(user, posts);
    }

    public List<UserWithPostsDto> getAllUsersWithPosts() {
        return userRepository.findAll()
                .stream()
                .map(user -> UserMapper.toDtoWithPosts(
                        user,
                        user.getPosts().stream().map(PostMapper::toDto).toList()))
                .toList();
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}