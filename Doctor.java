package com.project.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
    name = "doctors",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
    }
)
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Speciality is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String speciality;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "doctor_available_times",
            joinColumns = @JoinColumn(name = "doctor_id")
    )
    @Column(name = "available_time", nullable = false)
    private List<String> availableTimes = new ArrayList<>();

    // Default constructor
    public Doctor() {
    }

    // Parameterized constructor
    public Doctor(String fullName, String email, String speciality, List<String> availableTimes) {
        this.fullName = fullName;
        this.email = email;
        this.speciality = speciality;
        this.availableTimes = availableTimes;
    }

    // Getters and Setters

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(doctorId, doctor.doctorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", speciality='" + speciality + '\'' +
                ", availableTimes=" + availableTimes +
                '}';
    }
}
