package ru.lipnin.itmohomework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.lipnin.itmohomework.constants.Category;
import ru.lipnin.itmohomework.entity.BeautyService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BeautyServiceRepository extends JpaRepository<BeautyService, Long> {

    List<BeautyService> findAllBeautyServiceByCategory(Category category);

    Optional<BeautyService> findByIdAndRemovedFalse(Long id);

    @Query("SELECT s FROM BeautyService s " +
            "WHERE NOT EXISTS (" +
            "    SELECT 1 FROM Appointment a " +
            "    WHERE a.service = s " +
            "    AND a.appointmentTime = :appointmentTime " +
            "    AND a.status NOT IN ('COMPLETED', 'CANCELLED')" +
            ")")
    List<BeautyService> findAllNotReservedServicesByTime(LocalDateTime appointmentTime);

    @Query (nativeQuery = true,
    value = "SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END " +
            "FROM appointment a " +
            "JOIN services s ON s.id = a.service_id " +
            "WHERE :appointmentTime >= a.appointment_time " +
            "AND :appointmentTime < a.appointment_time + INTERVAL '1 MINUTE' * s.duration " +
            "AND a.status NOT IN ('COMPLETED', 'CANCELLED')" +
            "AND s.id = :id")
    boolean isServiceReserved(Long id, LocalDateTime appointmentTime);
}
