package edu.uth.skincarebookingsystem.dto.respone;  

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
public class PaymentResponse {
    private Long id;
    private Long appointmentId;
    private BigDecimal amount;
    private Payment.PaymentMethod paymentMethod;
    private Payment.PaymentStatus status;
    private LocalDateTime createdAt;

}