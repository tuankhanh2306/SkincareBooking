package edu.uth.skincarebookingsystem.repositories;

import edu.uth.skincarebookingsystem.models.Appointment;
import edu.uth.skincarebookingsystem.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
//    Optional<Payment> findByAppointment(Appointment appointmentEntity);
}
