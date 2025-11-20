package edu.uth.skincarebookingsystem.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 1. Implement UserDetails để Spring Security hiểu đây là người dùng
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum UserRole {
        CUSTOMER, SPECIALIST, ADMIN
    }

    // --- CÁC PHƯƠNG THỨC CỦA USERDETAILS (BẮT BUỘC PHẢI CÓ) ---

    // Trong file User.java (phải implements UserDetails)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // SAI: return List.of(new SimpleGrantedAuthority(role.name()));
        // -> Kết quả: "ADMIN" (Spring Security sẽ từ chối 403)

        // ĐÚNG: Phải nối thêm chuỗi "ROLE_"
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
        // -> Kết quả: "ROLE_ADMIN" (Spring Security chấp nhận)
    }

    @Override
    public String getUsername() {
        return email; // Dùng email làm username đăng nhập
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}