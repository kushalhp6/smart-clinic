package com.project.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a doctor in the Smart Clinic Management System.
 *
 * <p>This entity is mapped to the {@code doctors} database table and stores
 * information required to manage doctors and their appointment availability.</p>
 *
 * <p>Each doctor contains:</p>
 * <ul>
 *     <li>A unique generated identifier.</li>
 *     <li>Personal information such as name and email.</li>
 *     <li>The doctor's medical speciality.</li>
 *     <li>A collection of available appointment time slots.</li>
 * </ul>
 *
 * <p>The email address is unique across all doctors and is used during
 * authentication and communication.</p>
 *
 * <p>This entity is managed by JPA/Hibernate and participates in the
 * persistence layer of the Smart Clinic application.</p>
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
     * Primary key of the doctor.
     *
     * <p>The identifier is generated automatically using the database
     * identity strategy.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;

    /**
     * Full name of the doctor.
     */
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    /**
     * Unique email address of the doctor.
     *
     * <p>This value is validated before persistence and must be unique.</p>
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Medical speciality of the doctor
     * (e.g., Cardiology, Neurology, Orthopaedics).
     */
    @NotBlank(message = "Speciality is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String speciality;

    /**
     * List of appointment time slots during which the doctor
     * is available to see patients.
     *
     * <p>This collection is stored in a separate table named
     * {@code doctor_available_times} using JPA's
     * {@link ElementCollection} mapping.</p>
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
     * Creates a fully initialized Doctor object.
     *
     * @param fullName doctor's full name
     * @param email doctor's email address
     * @param speciality doctor's medical speciality
     * @param availableTimes list of available appointment slots
     */
    public Doctor(String fullName,
                  String email,
                  String speciality,
                  List<String> availableTimes) {
        this.fullName = fullName;
        this.email = email;
        this.speciality = speciality;
        this.availableTimes = availableTimes;
    }

    /**
     * Returns the doctor's unique identifier.
     *
     * @return doctor ID
     */
    public Long getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the doctor's unique identifier.
     *
     * @param doctorId unique identifier
     */
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Returns the doctor's full name.
     *
     * @return full name
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
     * Returns the doctor's email address.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the doctor's email address.
     *
     * @param email doctor's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the doctor's medical speciality.
     *
     * @return speciality
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
     * Returns all available appointment time slots.
     *
     * @return list of available appointment times
     */
    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    /**
     * Replaces the doctor's available appointment schedule.
     *
     * @param availableTimes updated appointment schedule
     */
    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }

    /**
     * Determines whether two Doctor objects represent
     * the same persisted entity.
     *
     * @param o object to compare
     * @return true if the IDs are equal; otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(doctorId, doctor.doctorId);
    }

    /**
     * Computes the hash code based on the primary key.
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
     * @return string representation of the doctor
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
