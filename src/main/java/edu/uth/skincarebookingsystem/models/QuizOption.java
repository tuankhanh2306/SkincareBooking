package edu.uth.skincarebookingsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quiz_options")
public class QuizOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String optionText;

    @Column(nullable = false)
    private int weight;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;

    @ManyToMany
    @JoinTable(name = "option_services",
        joinColumns = @JoinColumn(name = "option_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<DermatologyService> recommendedServices;

}
