package edu.uth.skincarebookingsystem.service;

import edu.uth.skincarebookingsystem.dto.request.FeedbackDto;
import edu.uth.skincarebookingsystem.exceptions.AppException;
import edu.uth.skincarebookingsystem.exceptions.ErrorCode;
import edu.uth.skincarebookingsystem.models.Feedback;
import edu.uth.skincarebookingsystem.models.Specialist;
import edu.uth.skincarebookingsystem.models.User;
import edu.uth.skincarebookingsystem.repositories.FeedbackRepository;
import edu.uth.skincarebookingsystem.repositories.SpecialistRepository;
import edu.uth.skincarebookingsystem.repositories.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Repository
public class FeedbackService {
    private FeedbackRepository feedbackRepository;
    private SpecialistRepository specialistRepository;
    private UserRepository userRepository;

    private FeedbackDto convertToDo (Feedback feedback){
        FeedbackDto feedbackDto = new FeedbackDto();

        feedbackDto.setId(feedback.getId());
        feedbackDto.setCustomerId(feedback.getCustomer().getId());
        feedbackDto.setSpecialistId(feedback.getSpecialist().getId());
        feedbackDto.setRating(feedback.getRating());
        feedbackDto.setComment(feedback.getComment());

        feedbackDto.setCustomerName(feedback.getCustomer().getFullName());
        feedbackDto.setSpecialistName(feedback.getSpecialist().getUser().getFullName());

        return feedbackDto;
    }

    public FeedbackDto createFeedback(FeedbackDto feedbackDto) {
        User user = userRepository.findById(feedbackDto.getCustomerId()).
                orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));

        Specialist specialist = specialistRepository.findById(feedbackDto.getSpecialistId().longValue())
                .orElseThrow(() -> new AppException(ErrorCode.SPECIALIST_NOT_FOUND));

        Feedback feedback = new Feedback();
        feedback.setSpecialist(specialist);
        feedback.setCustomer(user);
        feedback.setRating(feedbackDto.getRating());
        feedback.setComment(feedbackDto.getComment());

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return convertToDo(savedFeedback);
    }

    public List<FeedbackDto> getAllFeedbacks() {
        return feedbackRepository.findAll().stream()
                .map(this::convertToDo)
                .collect(Collectors.toList());
    }

    public List<FeedbackDto> getFeedbacksBySpecialistId(Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new AppException(ErrorCode.SPECIALIST_NOT_FOUND));

        return feedbackRepository.findBySpecialist(specialist).stream()
                .map(this::convertToDo)
                .collect(Collectors.toList());
    }

    public List<FeedbackDto> getFeedbacksByCustomerId(Long customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return feedbackRepository.findByCustomer(customer).stream()
                .map(this::convertToDo)
                .collect(Collectors.toList());
    }

    public Double getAverageRatingBySpecialistId(Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new AppException(ErrorCode.SPECIALIST_NOT_FOUND));

        return feedbackRepository.calculateAverageRatingForSpecialist(specialist);
    }

    public void deleteFeedback(Long feedbackId) {
        feedbackRepository.deleteById(feedbackId);
    }

}
