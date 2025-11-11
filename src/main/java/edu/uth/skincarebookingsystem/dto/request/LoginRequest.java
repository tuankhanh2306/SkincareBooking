package edu.uth.skincarebookingsystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
}
