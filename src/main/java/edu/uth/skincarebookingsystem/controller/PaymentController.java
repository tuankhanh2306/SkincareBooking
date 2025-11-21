package edu.uth.skincarebookingsystem.controller;

import edu.uth.skincarebookingsystem.dto.request.PaymentRequest;
import edu.uth.skincarebookingsystem.dto.respone.PaymentResponse;
import edu.uth.skincarebookingsystem.models.Payment;
import edu.uth.skincarebookingsystem.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:63342", "http://localhost:3000", "http://localhost:4200"},
        allowedHeaders = {"Authorization", "Content-Type", "X-Requested-With"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest payment) {
        return ResponseEntity.ok(paymentService.createPayment(payment));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.GetAllPayments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> updateStatus(@PathVariable Long id, @RequestParam Payment.PaymentStatus status) {
        return ResponseEntity.ok(paymentService.updateStatus(id, status));
    }

}
