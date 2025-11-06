package ru.lipnin.itmohomework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.lipnin.itmohomework.constants.Category;
import ru.lipnin.itmohomework.entity.BeautyService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BeautyServiceRepository extends JpaRepository<BeautyService, Long> {

    List<BeautyService> findAllBeautyServiceByCategoryAndRemovedFalse(Category category);

    Optional<BeautyService> findByIdAndRemovedFalse(Long id);

    @Query(nativeQuery = true,
     value ="SELECT * FROM services s " +
            "WHERE NOT EXISTS (" +
            "SELECT 1 " +
            "FROM appointment a " +
            "WHERE a.service_id = s.id " +
            "AND :appointmentTime >= a.appointment_time " +
            "AND :appointmentTime <= a.appointment_time + INTERVAL '1 MINUTE' * s.duration " +
            "AND a.status NOT IN ('COMPLETED', 'CANCELLED')) " +
            "AND s.removed = false")
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

    Set<BeautyService> findAllByIdInAndRemovedFalse(List<Long> ids);
}
