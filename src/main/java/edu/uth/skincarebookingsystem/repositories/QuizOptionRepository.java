package edu.uth.skincarebookingsystem.repositories;

import edu.uth.skincarebookingsystem.models.QuizOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizOptionRepository extends JpaRepository<QuizOption, Long> {
    List<QuizOption> findByQuestionId(Long questionId);
}
