package edu.uth.skincarebookingsystem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialistDto {
    private Long id;

    private UserCreateDto user;

    @NotBlank(message = "Chuyên môn không được để trống")
    private String expertise;

    @NotNull(message = "Số năm kinh nghiệm không được để trống")
    @Min(value = 0, message = "Số năm kinh nghiệm phải lớn hơn hoặc bằng 0")
    private Integer experience;

    private BigDecimal rating;
}
