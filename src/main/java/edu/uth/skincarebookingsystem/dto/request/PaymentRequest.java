package edu.uth.skincarebookingsystem.dto.request;

import edu.uth.skincarebookingsystem.models.Payment;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull(message = "Lịch hẹn không được để trống")
    private Long appointmentId;

    @NotNull(message = "Số tiền không được để trống")
    @Min(value = 0, message = "Số tiền phải lớn hơn hoặc bằng 0")
    private BigDecimal amount;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    private Payment.PaymentMethod paymentMethod;

}