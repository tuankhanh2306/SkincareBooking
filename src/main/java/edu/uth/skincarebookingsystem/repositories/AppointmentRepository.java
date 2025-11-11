package edu.uth.skincarebookingsystem.repositories;

import edu.uth.skincarebookingsystem.models.Appointment;
import edu.uth.skincarebookingsystem.models.DermatologyService;
import edu.uth.skincarebookingsystem.models.Specialist;
import edu.uth.skincarebookingsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

//Repository xử lý các truy vấn liên quan đến cuộc hẹn.
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    //Lấy danh sách cuộc hẹn theo khách hàng.
    List<Appointment> findByCustomer(User customer);

    //Lấy danh sách cuộc hẹn theo chuyên viên.
    List<Appointment> findBySpecialist(Specialist specialist);


     //Lấy danh sách cuộc hẹn của chuyên viên trong một khoảng thời gian nhất định.

    @Query("SELECT a FROM Appointment a WHERE a.specialist = ?1 AND a.appointmentDate BETWEEN ?2 AND ?3")
    List<Appointment> findSpecialistAppointmentsForDateRange(Specialist specialist, LocalDateTime start, LocalDateTime end);

    //Lấy danh sách cuộc hẹn theo trạng thái.
    List<Appointment> findByStatus(Appointment.AppointmentStatus status);

    List<Appointment> findBySpecialistIdAndStatusNot(Long specialistId, Appointment.AppointmentStatus appointmentStatus);
}
