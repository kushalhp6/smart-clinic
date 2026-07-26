package com.project.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a doctor within the Smart Clinic system.
 *
 * <p>This entity stores the doctor's personal information,
 * speciality, and available appointment time slots.
 * Each doctor has a unique email address and an automatically
 * generated primary key.</p>
 */
@Entity
@Table(
    name = "doctors",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
    }
)
public class Doctor {

    /**
     * Unique identifier for the doctor.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;

    /**
     * Doctor's full name.
     */
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    /**
     * Doctor's unique email address.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Medical speciality of the doctor.
     */
    @NotBlank(message = "Speciality is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String speciality;

    /**
     * Collection of available appointment time slots.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "doctor_available_times",
            joinColumns = @JoinColumn(name = "doctor_id")
    )
    @Column(name = "available_time", nullable = false)
    private List<String> availableTimes = new ArrayList<>();

    /**
     * Default constructor required by JPA.
     */
    public Doctor() {
    }

    /**
     * Creates a new Doctor.
     *
     * @param fullName doctor's full name
     * @param email doctor's email address
     * @param speciality doctor's medical speciality
     * @param availableTimes list of available appointment times
     */
    public Doctor(String fullName, String email,
                  String speciality, List<String> availableTimes) {
        this.fullName = fullName;
        this.email = email;
        this.speciality = speciality;
        this.availableTimes = availableTimes;
    }

    /**
     * @return the doctor's unique ID.
     */
    public Long getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the doctor's ID.
     *
     * @param doctorId unique identifier
     */
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * @return doctor's full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Updates the doctor's full name.
     *
     * @param fullName doctor's full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return doctor's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the doctor's email.
     *
     * @param email doctor's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return doctor's speciality.
     */
    public String getSpeciality() {
        return speciality;
    }

    /**
     * Updates the doctor's speciality.
     *
     * @param speciality medical speciality
     */
    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    /**
     * Returns the list of available appointment times.
     *
     * @return available appointment slots
     */
    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    /**
     * Updates the doctor's available appointment slots.
     *
     * @param availableTimes list of appointment times
     */
    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }

    /**
     * Compares two Doctor objects using their primary key.
     *
     * @param o object to compare
     * @return true if both objects represent the same doctor
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(doctorId, doctor.doctorId);
    }

    /**
     * Computes the hash code using the primary key.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(doctorId);
    }

    /**
     * Returns a readable string representation of the Doctor.
     *
     * @return doctor details
     */
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
