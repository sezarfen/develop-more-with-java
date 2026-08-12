package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients(){
        return patientRepository.findAll().stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO getPatient(UUID uuid){
        return PatientMapper.toDTO(patientRepository.getReferenceById(uuid));
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        // Kendi custom exception classımızı yazmak log trace yaparken iş görüyor.
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException("A patient with this " +
                    "email already exists " + patientRequestDTO.getEmail());
        }
        Patient patient =
                patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDTO(patient);

    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO){
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

        // bu email bu patient'a ait değil mi kontrol için PatientRepository'de existsByEmailAndIdNot(String, UUID) yazabiliriz
        // if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id))
        if (!patientRequestDTO.getEmail().equals(patient.getEmail()) && patientRepository.existsByEmail(patientRequestDTO.getEmail()))
        {
            throw new EmailAlreadyExistsException("A patient with this " +
                    "email already exists " + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        return PatientMapper.toDTO(patientRepository.save(patient));
    }

    public void deletePatient(UUID id){
        patientRepository.deleteById(id);
    }
}
