package ru.lipnin.itmohomework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.lipnin.itmohomework.entity.Appointment;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a " +
            "WHERE a.status NOT IN ('COMPLETED', 'CANCELLED') " +
            "and a.user = :user"
            )
    List<Appointment> findAllAppointmentByUserAndRemovedFalse(ApplicationUser user);

    List<Appointment> findAllAppointmentByAppointmentTimeAndRemovedFalse(LocalDateTime appointmentTime);

    Optional<Appointment> findByIdAndUserAndRemovedFalse(Long id, ApplicationUser user);

    Optional<Appointment> findByIdAndRemovedFalse(Long id);

//    @Query("SELECT SUM(s.price) FROM Appointment a " +
//            "JOIN a.service s " +
//            "WHERE (CAST(:from AS timestamp) IS NULL OR a.appointmentClose >= :from)" +
//            "AND a.appointmentClose <= :to " +
//            "AND a.status = 'COMPLETED'")
    @Query("SELECT SUM(a.price) FROM Appointment a " +
            "WHERE (CAST(:from AS timestamp) IS NULL OR a.appointmentClose >= :from)" +
            "AND a.appointmentClose <= :to " +
            "AND a.status = 'COMPLETED'")
    BigDecimal findAllEarnedMoneyByPeriod(LocalDateTime from, LocalDateTime to);

    Set<Appointment> findAllByIdInAndRemovedFalse(List<Long> listId);
}
