package edu.uth.skincarebookingsystem.repositories;

import edu.uth.skincarebookingsystem.models.DermatologyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<DermatologyService, Long> {
    List<DermatologyService> findByNameContainingIgnoreCase(String keyword);
    Optional<DermatologyService> findById(Long id);
}
