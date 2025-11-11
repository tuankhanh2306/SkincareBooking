package edu.uth.skincarebookingsystem.repositories;

import edu.uth.skincarebookingsystem.models.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

}
