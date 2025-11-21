package edu.uth.skincarebookingsystem.config;

import edu.uth.skincarebookingsystem.models.User;
import edu.uth.skincarebookingsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeedingConfig {

    // --- ADMIN: Lấy từ biến môi trường (Để bảo mật khi deploy thật) ---
    @Value("${SEED_ADMIN_EMAIL:admin@example.com}") // Mặc định nếu không có env
    private String adminEmail;

    @Value("${SEED_ADMIN_PASSWORD:admin123}")
    private String adminPassword;

    @Value("${SEED_ADMIN_FULLNAME:System Admin}")
    private String adminFullName;

    @Value("${SEED_ADMIN_PHONE:0850000001}")
    private String adminPhone;

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Tạo ADMIN (Lấy từ biến môi trường hoặc mặc định)
            createAccount(userRepository, passwordEncoder,
                    adminEmail, adminPassword, adminFullName, adminPhone, User.UserRole.ADMIN);

            // 2. Tạo SPECIALIST (Chuyên viên) - Fix cứng để test
            createAccount(userRepository, passwordEncoder,
                    "specialist@example.com", "123456", "Dr. Strange", "0900000002", User.UserRole.SPECIALIST);

            // 3. Tạo CUSTOMER (Khách hàng) - Fix cứng để test
            createAccount(userRepository, passwordEncoder,
                    "customer@example.com", "123456", "Nguyen Van Customer", "0900000003", User.UserRole.CUSTOMER);
        };
    }

    // Hàm phụ trợ để tạo user nếu chưa tồn tại (Giúp code gọn hơn)
    private void createAccount(UserRepository userRepository, PasswordEncoder passwordEncoder,
                               String email, String password, String fullName, String phone, User.UserRole role) {

        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhoneNumber(phone);
            user.setPassword(passwordEncoder.encode(password)); // Luôn mã hóa pass
            user.setRole(role);

            userRepository.save(user);
            System.out.println(">>> Đã tạo tài khoản mẫu: " + email + " (" + role + ")");
        } else {
            System.out.println(">>> Tài khoản " + email + " đã tồn tại. Bỏ qua.");
        }
    }
}