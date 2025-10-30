package ru.lipnin.itmohomework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.lipnin.itmohomework.entity.Appointment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a " +
            "WHERE a.status NOT IN ('COMPLETED', 'CANCELLED') " +
            "and a.clientName = :clientName"
            )
    List<Appointment> findAllAppointmentByClientNameAndRemovedFalse(String clientName);

    List<Appointment> findAllAppointmentByAppointmentTimeAndRemovedFalse(LocalDateTime appointmentTime);

    Optional<Appointment> findByIdAndRemovedFalse(Long id);

    @Query("SELECT SUM(s.price) FROM Appointment a " +
            "JOIN a.service s " +
            "WHERE (CAST(:from AS timestamp) IS NULL OR a.appointmentClose >= :from)" +
            "AND a.appointmentClose <= :to " +
            "AND a.status = 'COMPLETED'")
    BigDecimal findAllEarnedMoneyByPeriod(LocalDateTime from, LocalDateTime to);
}
