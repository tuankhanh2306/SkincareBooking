package edu.uth.skincarebookingsystem.controller;

import edu.uth.skincarebookingsystem.dto.request.ServiceDto;
import edu.uth.skincarebookingsystem.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = {"http://localhost:63342", "http://localhost:3000", "http://localhost:4200"},
        allowedHeaders = {"Authorization", "Content-Type", "X-Requested-With"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ServiceController {

    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceDto> createService( @Valid @RequestBody ServiceDto serviceDto) {
        ServiceDto service = serviceService.createService(serviceDto);
        return new ResponseEntity<>(service, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        List<ServiceDto> serviceDto = serviceService.getAllServices();
        return new ResponseEntity<>(serviceDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable Long id) {
        ServiceDto serviceDto = serviceService.getServiceById(id);
        return new ResponseEntity<>(serviceDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<List<ServiceDto>> searchServices(@RequestParam String keyword) {
        List<ServiceDto> serviceDto = serviceService.searchServices(keyword);
        return new ResponseEntity<>(serviceDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceDto> updateService(@PathVariable Long id,@Valid @RequestBody ServiceDto serviceDto) {
        ServiceDto updateService = serviceService.updateService(serviceDto, id);
        return new ResponseEntity<>(updateService, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceDto> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
