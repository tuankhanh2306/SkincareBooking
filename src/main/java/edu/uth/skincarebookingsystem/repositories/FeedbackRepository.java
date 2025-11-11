package edu.uth.skincarebookingsystem.repositories;

import edu.uth.skincarebookingsystem.models.Feedback;
import edu.uth.skincarebookingsystem.models.Specialist;
import edu.uth.skincarebookingsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findBySpecialist(Specialist specialist);
    List<Feedback> findByCustomer(User customer);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.specialist = ?1")
    Double calculateAverageRatingForSpecialist(Specialist specialist);
}
