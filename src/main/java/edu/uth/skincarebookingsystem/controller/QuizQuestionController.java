package edu.uth.skincarebookingsystem.controller;

import edu.uth.skincarebookingsystem.dto.request.QuizQuestionDto;
import edu.uth.skincarebookingsystem.dto.request.ServiceDto;
import edu.uth.skincarebookingsystem.service.QuizQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-question")
@RequiredArgsConstructor
public class QuizQuestionController {

    private final QuizQuestionService quizQuestionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuizQuestionDto> createQuizQuestion(@Valid @RequestBody QuizQuestionDto quizQuestion) {
        return new ResponseEntity<>(quizQuestionService.createQuestion(quizQuestion), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<List<QuizQuestionDto>> getAllQuizQuestions() {
        return new ResponseEntity<>(quizQuestionService.getAllQuestions(), HttpStatus.OK);
    }

    @PostMapping("/questions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuizQuestionDto> createQuestion(@RequestBody QuizQuestionDto questionDto) {
        QuizQuestionDto created = quizQuestionService.createQuestion(questionDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteQuestion(@PathVariable long id) {
        quizQuestionService.deleteQuestion(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
