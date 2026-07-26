package com.project.backend.services;

import com.project.backend.models.Doctor;
import com.project.backend.repositories.DoctorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service responsible for doctor-related business operations,
 * including appointment availability and authentication.
 */
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository,
                         TokenService tokenService,
                         PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Returns the doctor's available appointment slots for a given date.
     *
     * Expected slot format:
     * yyyy-MM-dd HH:mm
     *
     * @param doctorId Doctor's unique identifier
     * @param date Appointment date
     * @return List of available appointment slots
     */
    public List<String> getAvailableTimeSlots(Long doctorId, LocalDate date) {

        return doctorRepository.findById(doctorId)
                .map(doctor ->
                        doctor.getAvailableTimes()
                                .stream()
                                .filter(slot ->
                                        slot.startsWith(date.toString()))
                                .collect(Collectors.toList()))
                .orElse(List.of());
    }

    /**
     * Authenticates a doctor using email and password.
     *
     * @param email Doctor's email address
     * @param password Plain-text password supplied by the user
     * @return Authentication result and JWT token if successful
     */
    public Map<String, Object> login(String email, String password) {

        Optional<Doctor> doctorOptional =
                doctorRepository.findByEmail(email);

        if (doctorOptional.isEmpty()) {
            return Map.of(
                    "success", false,
                    "message", "Invalid email or password"
            );
        }

        Doctor doctor = doctorOptional.get();

        // Secure password verification using BCrypt
        if (!passwordEncoder.matches(password, doctor.getPassword())) {
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
