package edu.uth.skincarebookingsystem.service;

import edu.uth.skincarebookingsystem.dto.request.QuizQuestionDto;

import java.util.List;

public interface QuizQuestionService {
    QuizQuestionDto createQuestion(QuizQuestionDto quizQuestionDto);
    List<QuizQuestionDto> getAllQuestions();
    void deleteQuestion(long questionID);
}
