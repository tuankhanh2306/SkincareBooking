package edu.uth.skincarebookingsystem.service;

import edu.uth.skincarebookingsystem.dto.request.SpecialistDto;
import edu.uth.skincarebookingsystem.dto.request.UserCreateDto;
import edu.uth.skincarebookingsystem.exceptions.AppException;
import edu.uth.skincarebookingsystem.exceptions.ErrorCode;
import edu.uth.skincarebookingsystem.models.Specialist;
import edu.uth.skincarebookingsystem.models.User;
import edu.uth.skincarebookingsystem.repositories.SpecialistRepository;
import edu.uth.skincarebookingsystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecialistService {
    private final SpecialistRepository specialistRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SpecialistDto convertToDo(Specialist specialist){
        return SpecialistDto.builder() // Sử dụng @Builder
                .id(specialist.getId())
                .user(UserCreateDto.builder()
                        .id(specialist.getUser().getId())
                        .fullName(specialist.getUser().getFullName())
                        .email(specialist.getUser().getEmail())
                        .phoneNumber(specialist.getUser().getPhoneNumber())
                        .password(passwordEncoder.encode(specialist.getUser().getPassword()))
                        .role(specialist.getUser().getRole())
                        .build()
                )
                .expertise(specialist.getExpertise())
                .experience(specialist.getExperience())
                .rating(specialist.getRating())
                .build();
    }

    public SpecialistDto createSpecialist(SpecialistDto dto){
        User user = User.builder()
                .fullName(dto.getUser().getFullName())
                .password(passwordEncoder.encode(dto.getUser().getPassword()))
                .email(dto.getUser().getEmail())
                .phoneNumber(dto.getUser().getPhoneNumber())
                .role(User.UserRole.SPECIALIST)
                .build();
        userRepository.save(user);

        // 2. Gắn User vào Specialist
        Specialist specialist = Specialist.builder()
                .user(user)
                .expertise(dto.getExpertise())
                .experience(dto.getExperience())
                .rating(dto.getRating() == null ? BigDecimal.ZERO : dto.getRating())
                .build();
        specialistRepository.save(specialist);

        // 3. Trả lại SpecialistDto
        return convertToDo(specialist);
    }

    public List<SpecialistDto> getAllSpecialist(){
        return specialistRepository.findAll().stream()
                .map(this::convertToDo)
                .collect(Collectors.toList());
    }

    public SpecialistDto getSpecialistById(Long id){
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.SPECIALIST_NOT_FOUND));
        return convertToDo(specialist);
    }

    public SpecialistDto updateSpecialist(Long id,SpecialistDto specialistDto){
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.SPECIALIST_NOT_FOUND));
        specialist.setExpertise(specialistDto.getExpertise());
        specialist.setExperience(specialistDto.getExperience());
        specialist.setRating(specialistDto.getRating());
        return convertToDo(specialist);
    }

    public void deleteSpecialist(Long id){
        specialistRepository.deleteById(id);

    }

}

