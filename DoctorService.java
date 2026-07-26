package com.project.backend.services;

import com.project.backend.models.Doctor;
import com.project.backend.repositories.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    /**
     * Returns the doctor's available time slots for the specified date.
     *
     * Expected format of each available time:
     * yyyy-MM-dd HH:mm
     */
    public List<String> getAvailableTimeSlots(Long doctorId, String date) {

        return doctorRepository.findById(doctorId)
                .map(doctor -> doctor.getAvailableTimes()
                        .stream()
                        .filter(slot -> slot.startsWith(date))
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    /**
     * Validates doctor's login credentials.
     */
    public Map<String, Object> login(String email, String password) {

        Optional<Doctor> doctorOptional = doctorRepository.findByEmail(email);

        if (doctorOptional.isEmpty()) {
            return Map.of(
                    "success", false,
                    "message", "Invalid email or password"
            );
        }

        Doctor doctor = doctorOptional.get();

        /*
         * In a production application:
         * passwordEncoder.matches(password, doctor.getPassword())
         *
         * Simplified for the capstone project.
         */
        if (doctor.getPassword() == null ||
            !doctor.getPassword().equals(password)) {

            return Map.of(
                    "success", false,
                    "message", "Invalid email or password"
            );
        }

        String token = tokenService.generateToken(email);

        return Map.of(
                "success", true,
                "message", "Login successful",
                "token", token
        );
    }
}
