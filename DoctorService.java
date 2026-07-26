package com.project.backend.services;

import com.project.backend.models.Doctor;
import com.project.backend.repositories.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    // Return available time slots for a doctor on a given date
    public List<String> getAvailableTimeSlots(Long doctorId, String date) {

        Optional<Doctor> doctor = doctorRepository.findById(doctorId);

        if (doctor.isPresent()) {
            return doctor.get().getAvailableTimes();
        }

        return List.of();
    }

    // Validate doctor login credentials
    public Map<String, Object> login(String email, String password) {

        Optional<Doctor> doctor = doctorRepository.findByEmail(email);

        if (doctor.isPresent()) {

            // Normally password would be checked using BCrypt.
            // Simplified for the capstone project.
            String token = tokenService.generateToken(email);

            return Map.of(
                    "success", true,
                    "message", "Login successful",
                    "token", token
            );
        }

        return Map.of(
                "success", false,
                "message", "Invalid credentials"
        );
    }
}
