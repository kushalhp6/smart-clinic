package com.project.backend.repositories;

import com.project.backend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations and
 * custom lookup queries on Patient entities.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Retrieves a patient using their unique email address.
     *
     * @param email the patient's email address
     * @return an Optional containing the matching Patient if found,
     *         otherwise an empty Optional
     */
    Optional<Patient> findByEmail(String email);

    /**
     * Retrieves a patient by either their email address
     * or their phone number.
     *
     * This method is useful during authentication or
     * patient lookup when either identifier is available.
     *
     * @param email the patient's email address
     * @param phone the patient's phone number
     * @return an Optional containing the matching Patient if found,
     *         otherwise an empty Optional
     */
    Optional<Patient> findByEmailOrPhone(String email, String phone);
}
