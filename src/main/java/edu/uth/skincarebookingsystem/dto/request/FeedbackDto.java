package edu.uth.skincarebookingsystem.dto.request;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackDto {
    private Long id;

    @NotNull(message = "Khách hàng không được để trống")
    private Long customerId;

    @NotNull(message = "Chuyên viên không được để trống")
    private Long specialistId;

    @NotNull(message = "Đánh giá không được để trống")
    @Min(value = 1, message = "Đánh giá phải từ 1 đến 5 sao")
    @Max(value = 5, message = "Đánh giá phải từ 1 đến 5 sao")
    private Integer rating;

    private String comment;
    private LocalDateTime createdAt;

    // Thông tin bổ sung cho frontend
    private String customerName;
    private String specialistName;
}