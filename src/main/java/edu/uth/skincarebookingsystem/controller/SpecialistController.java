package edu.uth.skincarebookingsystem.controller;

import edu.uth.skincarebookingsystem.dto.request.SpecialistDto;
import edu.uth.skincarebookingsystem.models.Specialist;
import edu.uth.skincarebookingsystem.service.SpecialistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialists")
@RequiredArgsConstructor

@CrossOrigin(origins = {"http://localhost:63342", "http://localhost:3000", "http://localhost:4200"},
        allowedHeaders = {"Authorization", "Content-Type", "X-Requested-With"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class SpecialistController {
    private final SpecialistService specialistService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<SpecialistDto> createSpecialist(@Validated @RequestBody SpecialistDto specialist){
        SpecialistDto specialist1 = specialistService.createSpecialist(specialist);
        return new ResponseEntity<>(specialist1,HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SPECIALIST' ,'CUSTOMER')")
    ResponseEntity<List<SpecialistDto>> getAllSpecialist(){
        List<SpecialistDto> specialists = specialistService.getAllSpecialist();
        return new ResponseEntity<>(specialists,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SPECIALIST')")
    public ResponseEntity<SpecialistDto> getSpecialistById(@PathVariable Long id){
        SpecialistDto specialist = specialistService.getSpecialistById(id);
        return new ResponseEntity<>(specialist,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpecialistDto> updateSpecialist(@PathVariable Long id, @RequestBody SpecialistDto specialist){
        SpecialistDto specialist1 = specialistService.updateSpecialist(id, specialist);
        return new ResponseEntity<>(specialist1,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSpecialist(@PathVariable Long id){
        specialistService.deleteSpecialist(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
