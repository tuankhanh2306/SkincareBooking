package edu.uth.skincarebookingsystem.dto.request;

import edu.uth.skincarebookingsystem.models.Appointment;
import jakarta.validation.constraints.Future;
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
public class AppointmentDto {
    private Long id;

    @NotNull(message = "Khách hàng không được để trống")
    private Long customerId;

    @NotNull(message = "Chuyên viên không được để trống")
    private Long specialistId;

    @NotNull(message = "Dịch vụ không được để trống")
    private Long serviceId;

    @NotNull(message = "Ngày giờ hẹn không được để trống")
    @Future(message = "Ngày giờ hẹn phải là thời gian trong tương lai")
    private LocalDateTime appointmentDate;

    private Appointment.AppointmentStatus status;
    private LocalDateTime createdAt;

    private String customerName;
    private String specialistName;
    private String serviceName;
}