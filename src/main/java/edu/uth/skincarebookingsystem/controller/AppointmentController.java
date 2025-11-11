//
package edu.uth.skincarebookingsystem.controller;

import edu.uth.skincarebookingsystem.dto.request.AppointmentDto;
import edu.uth.skincarebookingsystem.models.Appointment;
import edu.uth.skincarebookingsystem.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        AppointmentDto updated = appointmentService.updateStatus(appointmentId, appointmentDto.getStatus());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PutMapping("/{appointmentId}/complete")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'ADMIN')")
    public ResponseEntity<AppointmentDto> completeAppointment(@PathVariable("appointmentId") Long appointmentId) {
        AppointmentDto updated = appointmentService.updateStatus(appointmentId, Appointment.AppointmentStatus.COMPLETED);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'ADMIN')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
