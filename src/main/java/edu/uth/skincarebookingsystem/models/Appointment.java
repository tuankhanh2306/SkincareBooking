package edu.uth.skincarebookingsystem.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private DermatologyService service;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum AppointmentStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }
}
