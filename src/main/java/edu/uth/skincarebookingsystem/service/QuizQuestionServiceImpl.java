package edu.uth.skincarebookingsystem.service;

import edu.uth.skincarebookingsystem.dto.request.QuizOptionDto;
import edu.uth.skincarebookingsystem.dto.request.QuizQuestionDto;
import edu.uth.skincarebookingsystem.models.DermatologyService;
import edu.uth.skincarebookingsystem.models.QuizOption;
import edu.uth.skincarebookingsystem.models.QuizQuestion;
import edu.uth.skincarebookingsystem.repositories.QuizOptionRepository;
import edu.uth.skincarebookingsystem.repositories.QuizQuestionRepository;
import edu.uth.skincarebookingsystem.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizQuestionServiceImpl implements QuizQuestionService {
    private final QuizOptionRepository quizOptionRepository;
    private final ServiceRepository serviceRepository;
    private final QuizQuestionRepository quizQuestionRepository;

    @Override
    public QuizQuestionDto createQuestion(QuizQuestionDto dto) {
        QuizQuestion question = new QuizQuestion();
        question.setQuestion(dto.getQuestion());

        List<QuizOption> options = dto.getOptions().stream().map(optionDto -> {
            QuizOption option = new QuizOption();
            option.setOptionText(optionDto.getOptionText());
            option.setWeight(optionDto.getWeight());
            option.setQuestion(question); // thiết lập quan hệ ngược

            List<DermatologyService> services = serviceRepository.findAllById(optionDto.getRecommendedServiceIds());
            option.setRecommendedServices(services);

            return option;
        }).collect(Collectors.toList());

        question.setOptions(options);
        QuizQuestion saved = quizQuestionRepository.save(question);

        dto.setId(saved.getId());
        return dto;
    }

    @Override
    public List<QuizQuestionDto> getAllQuestions() {
        return quizQuestionRepository.findAll().stream().map(q -> {
            QuizQuestionDto dto = new QuizQuestionDto();
            dto.setId(q.getId());
            dto.setQuestion(q.getQuestion());

            List<QuizOptionDto> optionDtos = q.getOptions().stream().map(opt -> {
                QuizOptionDto optionDto = new QuizOptionDto();
                optionDto.setId(opt.getId());
                optionDto.setOptionText(opt.getOptionText());
                optionDto.setWeight(opt.getWeight());
                optionDto.setRecommendedServiceIds(opt.getRecommendedServices().stream()
                        .map(DermatologyService::getId).collect(Collectors.toList()));
                return optionDto;
            }).collect(Collectors.toList());

            dto.setOptions(optionDtos);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteQuestion(long questionID) {
        quizOptionRepository.deleteById(questionID);
    }


}
