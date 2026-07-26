package com.project.backend.controllers;

import com.project.backend.services.DoctorService;
import com.project.backend.services.TokenService;
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

    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable Long doctorId,
            @RequestParam String date,
            @RequestHeader("Authorization") String token) {

        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "success", false,
                            "message", "Invalid token"
                    ));
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "doctorId", doctorId,
                        "date", date,
                        "availableTimes",
                        doctorService.getAvailableTimeSlots(doctorId, date)
                )
        );
    }
}
