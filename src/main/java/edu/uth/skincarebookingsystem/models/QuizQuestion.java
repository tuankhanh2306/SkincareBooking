package edu.uth.skincarebookingsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quiz_questions")
@Builder
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @OneToMany(mappedBy = "question" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List <QuizOption> options;

}
