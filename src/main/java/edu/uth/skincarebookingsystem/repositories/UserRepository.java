package edu.uth.skincarebookingsystem.repositories;

import edu.uth.skincarebookingsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    Optional<User> findByFullName(String fullName);
}