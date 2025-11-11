package edu.uth.skincarebookingsystem.dto.respone;

import edu.uth.skincarebookingsystem.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;


    private User.UserRole role;
    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }
}
