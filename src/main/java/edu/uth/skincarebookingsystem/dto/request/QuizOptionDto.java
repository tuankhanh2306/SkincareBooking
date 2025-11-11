package edu.uth.skincarebookingsystem.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class QuizOptionDto {
    private Long id;
    private String optionText;
    private int weight;
    private List<Long > recommendedServiceIds;
}
