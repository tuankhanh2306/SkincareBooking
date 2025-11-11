package edu.uth.skincarebookingsystem.controller;

import edu.uth.skincarebookingsystem.dto.request.FeedbackDto;
import edu.uth.skincarebookingsystem.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    // CUSTOMER gửi feedback
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<FeedbackDto> createFeedback(@RequestBody FeedbackDto feedbackDto) {
        FeedbackDto created = feedbackService.createFeedback(feedbackDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // ADMIN hoặc CUSTOMER xem tất cả feedback
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<List<FeedbackDto>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    // Lấy feedback theo chuyên viên
    @GetMapping("/specialist/{specialistId}")
    public ResponseEntity<List<FeedbackDto>> getFeedbacksBySpecialist(@PathVariable Long specialistId) {
        return ResponseEntity.ok(feedbackService.getFeedbacksBySpecialistId(specialistId));
    }

    // Lấy feedback theo khách hàng
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<List<FeedbackDto>> getFeedbacksByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(feedbackService.getFeedbacksByCustomerId(customerId));
    }

    // Tính điểm đánh giá trung bình của một chuyên viên
    @GetMapping("/specialist/{specialistId}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long specialistId) {
        return ResponseEntity.ok(feedbackService.getAverageRatingBySpecialistId(specialistId));
    }

    // ADMIN xoá feedback
    @DeleteMapping("/{feedbackId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
