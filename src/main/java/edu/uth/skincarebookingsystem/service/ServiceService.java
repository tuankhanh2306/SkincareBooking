package edu.uth.skincarebookingsystem.service;

import edu.uth.skincarebookingsystem.dto.request.ServiceDto;
import edu.uth.skincarebookingsystem.exceptions.ResourceNotFoundException;
import edu.uth.skincarebookingsystem.models.DermatologyService;
import edu.uth.skincarebookingsystem.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    private ServiceDto convertToDto(DermatologyService service) {
        return ServiceDto.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .duration(service.getDuration())
                .build();
    }


    private DermatologyService convertToEntity(ServiceDto dto) {
        DermatologyService service = new DermatologyService();
        service.setId(dto.getId());
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        service.setDuration(dto.getDuration());
        return service;
    }

    public ServiceDto createService(ServiceDto serviceDto) {
        DermatologyService service = convertToEntity(serviceDto);
        DermatologyService savedService = serviceRepository.save(service);
        return convertToDto(savedService);
    }

    public ServiceDto getServiceById(Long id) {
        DermatologyService service = serviceRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Không tìm thấy dịch vụ với ID: " + id));
        return convertToDto(service);
    }

    public List<ServiceDto> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ServiceDto> searchServices(String keyword) {
        return serviceRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ServiceDto updateService(ServiceDto serviceDto,Long id) {
        DermatologyService service = serviceRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Không tìm thấy dịch vụ với ID: " + id));
        service.setName(serviceDto.getName());
        service.setDescription(serviceDto.getDescription());
        service.setPrice(serviceDto.getPrice());
        service.setDuration(serviceDto.getDuration());
        DermatologyService savedService = serviceRepository.save(service);
        return convertToDto(savedService);
    }

    public void deleteService(Long id) {
        if(!serviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dịch vụ", "ID", id);
        }
        serviceRepository.deleteById(id);
    }

}
