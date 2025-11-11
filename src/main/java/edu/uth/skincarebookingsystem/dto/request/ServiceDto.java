package edu.uth.skincarebookingsystem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDto {
    private Long id;

    @NotBlank(message = "Tên dịch vụ không được để trống")
    private String name;

    private String description;

    @NotNull(message = "Giá dịch vụ không được để trống")
    @Min(value = 0, message = "Giá dịch vụ phải lớn hơn hoặc bằng 0")
    private BigDecimal price;

    @NotNull(message = "Thời gian thực hiện không được để trống")
    @Min(value = 1, message = "Thời gian thực hiện phải lớn hơn 0")
    private Integer duration;
}
