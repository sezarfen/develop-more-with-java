package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import com.pm.patientservice.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService)
    {
        this.patientService = patientService;
    }

    @GetMapping("")
    public ResponseEntity<List<PatientResponseDTO>> getPatients(){
        return ResponseEntity.ok().body(patientService.getPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatients(@PathVariable UUID id){
        return ResponseEntity.ok().body(patientService.getPatient(id));
    }

    // normal validation'a ek olarak CratePatientValidation grubu olan validasyonu da yapıyor registeredDate yani
    @PostMapping("")
    public ResponseEntity<PatientResponseDTO> postPatients(
            @Validated({Default.class, CreatePatientValidationGroup.class}) // run all default validation and also additional for createpatient
            @RequestBody PatientRequestDTO patientRequestDTO) {

        return ResponseEntity.ok().body(patientService.createPatient(patientRequestDTO));
    }

    // @Validated({Default.class}), tells spring the validate request by provided on requestDTO
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO, @PathVariable UUID id){
        return ResponseEntity.ok().body(patientService.updatePatient(id, patientRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id){
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
