package ru.lipnin.itmohomework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.lipnin.itmohomework.entity.Appointment;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a " +
            "WHERE a.status NOT IN ('COMPLETED', 'CANCELLED') " +
            "and a.clientName = :clientName"
            )
    List<Appointment> findAllAppointmentByClientNameAndRemovedFalse(String clientName);

    Optional<Appointment> findByIdAndRemovedFalse(Long id);
}
