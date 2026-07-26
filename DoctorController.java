package com.project.backend.controllers;

import com.project.backend.services.DoctorService;
import com.project.backend.services.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final TokenService tokenService;

    public DoctorController(DoctorService doctorService,
                            TokenService tokenService) {
        this.doctorService = doctorService;
        this.tokenService = tokenService;
    }

    @GetMapping("/{userId}/{doctorId}/availability")
    public ResponseEntity<?> getDoctorAvailability(

            @PathVariable Long userId,
            @PathVariable Long doctorId,
            @RequestParam String date,
            @RequestHeader("Authorization") String token) {

        // Validate JWT token
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "message", "Invalid token"
                    ));
        }

        // Validate user role
        if (!tokenService.hasRole(token, "PATIENT")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "success", false,
                            "message", "Access denied. User must have PATIENT role."
                    ));
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "userId", userId,
                        "doctorId", doctorId,
                        "date", date,
                        "availableTimes",
                        doctorService.getAvailableTimeSlots(doctorId, date)
                )
        );
    }
}
