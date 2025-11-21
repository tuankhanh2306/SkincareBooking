//
package edu.uth.skincarebookingsystem.controller;

import edu.uth.skincarebookingsystem.dto.request.AppointmentDto;
import edu.uth.skincarebookingsystem.models.Appointment;
import edu.uth.skincarebookingsystem.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = {"http://localhost:63342", "http://localhost:3000", "http://localhost:4200"},
        allowedHeaders = {"Authorization", "Content-Type", "X-Requested-With"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;

    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentDto appointment) {
        AppointmentDto appointmentDto = appointmentService.createAppointment(appointment);
        return new ResponseEntity<>(appointmentDto, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','SPECIALIST', 'ADMIN')")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        List<AppointmentDto> appointments = appointmentService.getAllAppointments();
        return new ResponseEntity<>(appointments,HttpStatus.OK);
    }

    @GetMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER', 'SPECIALIST')")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable("appointmentId") Long appointmentId) {
        AppointmentDto appointmentDto = appointmentService.getAppointmentById(appointmentId);
        return new ResponseEntity<>(appointmentDto, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'SPECIALIST')")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByCustomerId(@PathVariable("customerId") Long customerId) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByCustomerId(customerId);
        return new ResponseEntity<>(appointments,HttpStatus.OK);
    }

    @GetMapping("/specialist/{specialistId}")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'ADMIN')")
    public ResponseEntity<List<AppointmentDto>> getAppointmentBySpecialistId(@PathVariable("specialistId") Long specialistId) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsBySpecialistId(specialistId);
        return new ResponseEntity<>(appointments,HttpStatus.OK);
    }

    @PutMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'ADMIN')")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable("appointmentId") Long appointmentId, @RequestBody AppointmentDto appointmentDto) {
        AppointmentDto appointmentDto1 = appointmentService.updateAppointment(appointmentId, appointmentDto);
        return new ResponseEntity<>(appointmentDto1, HttpStatus.OK);
    }

    @PutMapping("/{appointmentId}/status")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'ADMIN')")
    public ResponseEntity<AppointmentDto> updateAppointmentStatus(
            @PathVariable("appointmentId") Long appointmentId,
            @RequestBody AppointmentDto appointmentDto) {
        // Hàm này lấy status từ body gửi lên (JSON) để cập nhật
        AppointmentDto updated = appointmentService.updateStatus(appointmentId, appointmentDto.getStatus());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PutMapping("/{appointmentId}/complete")
    // SỬA: Thêm 'CUSTOMER' vào đây để khách hàng có quyền tự hoàn tất đơn hàng sau khi thanh toán
    @PreAuthorize("hasAnyRole('SPECIALIST', 'ADMIN', 'CUSTOMER')")
    public ResponseEntity<AppointmentDto> completeAppointment(@PathVariable("appointmentId") Long appointmentId) {
        // Lưu ý: Status sau khi thanh toán nên là CONFIRMED hoặc COMPLETED tùy logic của bạn.
        // Ở đây giữ nguyên là COMPLETED như bạn muốn.
        AppointmentDto updated = appointmentService.updateStatus(appointmentId, Appointment.AppointmentStatus.COMPLETED);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'ADMIN')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long specialistId,

            // THÊM DÒNG NÀY: @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime appointmentDateTime
    ) {
        // Gọi service xử lý...
        boolean isAvailable = appointmentService.checkAvailability(specialistId, appointmentDateTime);
        return ResponseEntity.ok(isAvailable);
    }



}
