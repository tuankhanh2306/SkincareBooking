//

package edu.uth.skincarebookingsystem.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "specialists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specialist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialist_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String expertise;

    @Column(nullable = false)
    private Integer experience;

    @Column
    private BigDecimal rating = BigDecimal.ZERO;
}