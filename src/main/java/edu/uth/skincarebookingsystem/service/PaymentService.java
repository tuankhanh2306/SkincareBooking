package edu.uth.skincarebookingsystem.service;

import edu.uth.skincarebookingsystem.dto.request.PaymentRequest;
import edu.uth.skincarebookingsystem.dto.respone.PaymentResponse;
import edu.uth.skincarebookingsystem.exceptions.AppException;
import edu.uth.skincarebookingsystem.exceptions.ErrorCode;
import edu.uth.skincarebookingsystem.models.Appointment;
import edu.uth.skincarebookingsystem.models.Payment;
import edu.uth.skincarebookingsystem.repositories.AppointmentRepository;
import edu.uth.skincarebookingsystem.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;

    private PaymentResponse convertToDto(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId().longValue())
                .appointmentId(payment.getAppointmentEntity().getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public PaymentResponse createPayment(PaymentRequest request){
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(()-> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        Payment payment = Payment.builder()
                .appointmentEntity(appointment)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(Payment.PaymentStatus.PAID)
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        return convertToDto(savedPayment);
    }

    public List<PaymentResponse>  GetAllPayments(){
        return paymentRepository.findAll().stream()
                .map(payment -> convertToDto(payment))
                .collect(Collectors.toList());

    }

    public PaymentResponse getPaymentById(long id){
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        return convertToDto(payment);
    }

    public PaymentResponse updateStatus(Long id, Payment.PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_FAILED));
        payment.setStatus(status);
        Payment updated = paymentRepository.save(payment);
        return convertToDto(updated);
    }
}
