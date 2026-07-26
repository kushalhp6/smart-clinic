package com.project.backend.controllers;

import com.project.backend.models.Prescription;
import com.project.backend.services.PrescriptionService;
import com.project.backend.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final TokenService tokenService;

    public PrescriptionController(PrescriptionService prescriptionService,
                                  TokenService tokenService) {
        this.prescriptionService = prescriptionService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<?> createPrescription(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody Prescription prescription) {

        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(401).body(
                    Map.of(
                            "success", false,
                            "message", "Invalid token"
                    )
            );
        }

        Prescription savedPrescription =
                prescriptionService.savePrescription(prescription);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Prescription saved successfully",
                        "data", savedPrescription
                )
        );
    }
}
