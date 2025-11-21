package edu.uth.skincarebookingsystem.service;

import edu.uth.skincarebookingsystem.dto.request.AppointmentDto;
import edu.uth.skincarebookingsystem.models.Appointment;
import edu.uth.skincarebookingsystem.models.DermatologyService;
import edu.uth.skincarebookingsystem.models.Specialist;
import edu.uth.skincarebookingsystem.models.User;
import edu.uth.skincarebookingsystem.repositories.AppointmentRepository;
import edu.uth.skincarebookingsystem.repositories.ServiceRepository;
import edu.uth.skincarebookingsystem.repositories.SpecialistRepository;
import edu.uth.skincarebookingsystem.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final SpecialistRepository specialistRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              ServiceRepository serviceRepository,
                              UserRepository userRepository,
                              SpecialistRepository specialistRepository) {
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.specialistRepository = specialistRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng hiện tại"));
    }

    public AppointmentDto convertToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setCustomerId(appointment.getCustomer().getId());
        dto.setServiceId(appointment.getService().getId());
        dto.setSpecialistId(appointment.getSpecialist().getId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setStatus(appointment.getStatus());
        dto.setCreatedAt(appointment.getCreatedAt());
        dto.setSpecialistName(appointment.getSpecialist().getUser().getFullName());
        dto.setServiceName(appointment.getService().getName());
        dto.setCustomerName(appointment.getCustomer().getFullName());
        return dto;
    }

    @Transactional
    public AppointmentDto createAppointment(@Valid AppointmentDto appointmentDto) {
        User customer = userRepository.findById(appointmentDto.getCustomerId().longValue())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        Specialist specialist = specialistRepository.findById(appointmentDto.getSpecialistId().longValue())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên viên"));
        DermatologyService service = serviceRepository.findById(appointmentDto.getServiceId().longValue())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ"));
        // Check if specialist is available at the requested time
        if (!isSpecialistAvailable(specialist.getId(), appointmentDto.getAppointmentDate(), service.getDuration())) {
            throw new RuntimeException("Chuyên viên đã có lịch hẹn vào thời gian này. Vui lòng chọn thời gian khác.");
        }
        Appointment appointment = new Appointment();
        appointment.setService(service);
        appointment.setCustomer(customer);
        appointment.setSpecialist(specialist);
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setStatus(appointmentDto.getStatus());
        appointment.setCreatedAt(appointmentDto.getCreatedAt());

        return convertToDto(appointmentRepository.save(appointment));
    }

    private boolean isSpecialistAvailable(Long specialistId, LocalDateTime appointmentDateTime, Integer duration) {
        // Calculate the end time of the requested appointment
        LocalDateTime appointmentEndTime = appointmentDateTime.plusMinutes(duration);

        // Find existing appointments for this specialist that overlap with the requested time
        List<Appointment> existingAppointments = appointmentRepository.findBySpecialistIdAndStatusNot(
                specialistId,  // Sử dụng specialistId thay vì specialistUserId
                Appointment.AppointmentStatus.CANCELLED
        );

        // Kiểm tra log
        System.out.println("Checking availability for specialist ID: " + specialistId);
        System.out.println("Appointment time: " + appointmentDateTime + " to " + appointmentEndTime);
        System.out.println("Found " + existingAppointments.size() + " existing appointments");

        // Check for any overlapping appointments
        for (Appointment existing : existingAppointments) {
            LocalDateTime existingStart = existing.getAppointmentDate();
            LocalDateTime existingEnd = existingStart.plusMinutes(existing.getService().getDuration());

            System.out.println("Existing appointment: " + existingStart + " to " + existingEnd);

            // Check if appointments overlap
            boolean overlap = (appointmentDateTime.isBefore(existingEnd) &&
                    appointmentEndTime.isAfter(existingStart));

            if (overlap) {
                System.out.println("Overlap detected!");
                return false; // Specialist is not available
            }
        }

        return true; // No overlapping appointments found
    }

    public AppointmentDto getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        User currentUser = getCurrentUser();
        boolean isOwner = appointment.getCustomer().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        boolean isSpecialist = appointment.getSpecialist().getUser().getId().equals(currentUser.getId());

        if (!isOwner && !isAdmin && !isSpecialist) {
            throw new RuntimeException("Bạn không có quyền xem lịch hẹn này");
        }

        return convertToDto(appointment);
    }

    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAppointmentsByCustomerId(Long customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        return appointmentRepository.findByCustomer(customer).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAppointmentsBySpecialistId(Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên viên"));
        return appointmentRepository.findBySpecialist(specialist).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentDto updateAppointment(Long id, @Valid AppointmentDto appointmentDto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        User customer = userRepository.findById(appointmentDto.getCustomerId().longValue())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        Specialist specialist = specialistRepository.findById(appointmentDto.getSpecialistId().longValue())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên viên"));
        DermatologyService service = serviceRepository.findById(appointmentDto.getServiceId().longValue())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ"));

        appointment.setCustomer(customer);
        appointment.setSpecialist(specialist);
        appointment.setService(service);
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        if (appointmentDto.getStatus() != null) {
            appointment.setStatus(appointmentDto.getStatus());
        }

        return convertToDto(appointmentRepository.save(appointment));
    }

    @Transactional
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        appointmentRepository.delete(appointment);
    }

    @Transactional
    public AppointmentDto updateStatus(Long id, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        appointment.setStatus(status);
        return convertToDto(appointmentRepository.save(appointment));
    }

    // ... bên trong class AppointmentService ...

    public boolean checkAvailability(Long specialistId, LocalDateTime appointmentDateTime) {
        // Giả sử mỗi cuộc hẹn kéo dài 1 tiếng (hoặc bạn có thể tùy chỉnh logic này)
        LocalDateTime start = appointmentDateTime;
        LocalDateTime end = appointmentDateTime.plusMinutes(60); // Ví dụ dịch vụ 60 phút

        // Kiểm tra trong database xem có cuộc hẹn nào trùng giờ (và chưa bị hủy) không
        boolean exists = appointmentRepository.existsBySpecialistIdAndDateRange(specialistId, start, end);

        // Nếu tồn tại cuộc hẹn (exists = true) -> Bận -> Trả về false (không available)
        // Nếu không tồn tại (exists = false) -> Rảnh -> Trả về true
        return !exists;
    }
}
