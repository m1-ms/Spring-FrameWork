package org.example.userpost.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    private Long id;

    @NotNull(message = "text is required")
    @Size(min = 20, message = "text length must be at least 20 characters")
    private String text;

    private String imagePath;

    private Long userId;
}