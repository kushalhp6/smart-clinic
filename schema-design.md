# Smart Clinic Management System - MySQL Database Schema

## Database: smart_clinic_db

---

## Table: Doctor

| Field | Data Type | Constraints |
|--------|-----------|------------|
| doctor_id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| full_name | VARCHAR(100) | NOT NULL |
| email | VARCHAR(100) | UNIQUE, NOT NULL |
| phone | VARCHAR(20) | UNIQUE |
| speciality | VARCHAR(100) | NOT NULL |
| available_times | TEXT | NULL |

---

## Table: Patient

| Field | Data Type | Constraints |
|--------|-----------|------------|
| patient_id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| full_name | VARCHAR(100) | NOT NULL |
| email | VARCHAR(100) | UNIQUE, NOT NULL |
| phone | VARCHAR(20) | UNIQUE |
| date_of_birth | DATE | NOT NULL |

---

## Table: Appointment

| Field | Data Type | Constraints |
|--------|-----------|------------|
| appointment_id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| doctor_id | BIGINT | FOREIGN KEY REFERENCES Doctor(doctor_id) |
| patient_id | BIGINT | FOREIGN KEY REFERENCES Patient(patient_id) |
| appointment_time | DATETIME | NOT NULL |
| status | VARCHAR(20) | NOT NULL |

---

## Table: Prescription

| Field | Data Type | Constraints |
|--------|-----------|------------|
| prescription_id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| appointment_id | BIGINT | FOREIGN KEY REFERENCES Appointment(appointment_id) |
| medicines | TEXT | NOT NULL |
| notes | TEXT | NULL |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

---

# Entity Relationships

Doctor (1) --------< Appointment >-------- (1) Patient

Appointment (1) --------< Prescription

- One Doctor can have many Appointments.
- One Patient can have many Appointments.
- Each Appointment belongs to one Doctor and one Patient.
- Each Prescription is linked to one Appointment.
