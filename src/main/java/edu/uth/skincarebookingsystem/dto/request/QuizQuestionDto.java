package edu.uth.skincarebookingsystem.dto.request;

import edu.uth.skincarebookingsystem.models.QuizOption;
import lombok.Data;

import java.util.List;

@Data
public class QuizQuestionDto {
    private Long id ;
    private String question ;
    private List<QuizOptionDto> options;
}
