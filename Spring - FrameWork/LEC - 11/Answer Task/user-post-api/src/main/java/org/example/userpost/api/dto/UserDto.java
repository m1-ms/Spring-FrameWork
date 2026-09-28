package org.example.userpost.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class UserDto {

    private Long id;

    @NotBlank(message = "name is required")
    @Size(min = 8, message = "name length must be greater than 7 characters")
    private String name;

    @Min(value = 18, message = "age must be at least 18")
    private Integer age;

    @NotBlank(message = "password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).+$",
            message = "password must contain an uppercase letter, a lowercase letter, a number and a special character"
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}