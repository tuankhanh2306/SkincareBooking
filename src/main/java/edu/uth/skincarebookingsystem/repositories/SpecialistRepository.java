package edu.uth.skincarebookingsystem.repositories;

import edu.uth.skincarebookingsystem.models.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {

    Optional<Specialist> findByUserId(Long userId);

}