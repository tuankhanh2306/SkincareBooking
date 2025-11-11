package edu.uth.skincarebookingsystem.controller;

import edu.uth.skincarebookingsystem.dto.request.ChangePasswordRequest;
import edu.uth.skincarebookingsystem.dto.request.UserCreateDto;
import edu.uth.skincarebookingsystem.dto.respone.UserResponseDto;
import edu.uth.skincarebookingsystem.exceptions.AppException;
import edu.uth.skincarebookingsystem.exceptions.ErrorCode;
import edu.uth.skincarebookingsystem.models.User;
import edu.uth.skincarebookingsystem.repositories.UserRepository;
import edu.uth.skincarebookingsystem.until.JwtUtils;
import edu.uth.skincarebookingsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:63342", "http://localhost:3000", "http://localhost:4200"},
        allowedHeaders = {"Authorization", "Content-Type", "X-Requested-With"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class UserController {
    private  final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<UserResponseDto> getCurrentUser(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        String email = jwtUtils.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return ResponseEntity.ok(UserResponseDto.fromEntity(user));
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new AppException(ErrorCode.EMAIL_INVALID);
    }

    // Lấy user theo ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Tạo user mới
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto userCreateDTO) {
        UserResponseDto createdUser = userService.createUser(userCreateDTO);
        return ResponseEntity.ok(createdUser);
    }

    // Cập nhật user
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,@Valid @RequestBody UserCreateDto userDto) {
        UserResponseDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    // Xóa user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/change-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(@PathVariable Long id,@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.ok("Cập nhật mật khẩu thành công !");
    }
}
