package edu.uth.skincarebookingsystem.config;

import edu.uth.skincarebookingsystem.models.User;
import edu.uth.skincarebookingsystem.repositories.UserRepository; // <-- KIỂM TRA LẠI ĐƯỜNG DẪN NÀY
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataSeedingConfig {

    // Đọc thông tin admin từ biến môi trường của Render
    // Chúng ta sẽ thêm các biến này vào Render ở bước sau
    @Value("${SEED_ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${SEED_ADMIN_PASSWORD}")
    private String adminPassword;

    @Value("${SEED_ADMIN_FULLNAME}")
    private String adminFullName;

    @Value("${SEED_ADMIN_PHONE}")
    private String adminPhone;

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {
            // Chỉ tạo admin nếu email này chưa tồn tại
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                System.out.println(">>> Creating ADMIN user...");

                User admin = new User();
                admin.setFullName(adminFullName);
                admin.setEmail(adminEmail);
                admin.setPhoneNumber(adminPhone);

                // Rất quan trọng: Luôn mã hóa mật khẩu trước khi lưu
                admin.setPassword(passwordEncoder.encode(adminPassword));

                // Sử dụng enum lồng bên trong class User
                admin.setRole(User.UserRole.ADMIN);

                // createdAt sẽ tự động được gán bởi @CreationTimestamp

                userRepository.save(admin);
                System.out.println(">>> ADMIN user created: " + admin.getEmail());
            } else {
                System.out.println(">>> Admin user '" + adminEmail + "' already exists. Skipping seeding.");
            }
        };
    }
}