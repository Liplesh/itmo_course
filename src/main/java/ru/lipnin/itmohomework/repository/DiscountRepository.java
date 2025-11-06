package ru.lipnin.itmohomework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.lipnin.itmohomework.entity.Discount;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    // Все скидки для конкретной услуги (SERVICE)
    @Query("SELECT d FROM Discount d " +
            "JOIN d.discountServices s " +
            "WHERE s.id = :serviceId " +
            "AND d.type = 'SERVICE' " +
            "AND d.removed = false " +
            "AND (d.startDate IS NULL OR d.startDate <= :currentTime) " +
            "AND (d.endDate IS NULL OR d.endDate >= :currentTime)")
    Set<Discount> findActiveDiscountsByServiceId(Long serviceId, LocalDateTime currentTime);

    // Все скидки для конкретной брони (APPOINTMENT)
    @Query("SELECT d FROM Discount d " +
            "JOIN d.discountAppointments a " +
            "WHERE a.id = :appointmentId " +
            "AND d.type = 'APPOINTMENT' " +
            "AND d.removed = false " +
            "AND (d.startDate IS NULL OR d.startDate <= :currentTime) " +
            "AND (d.endDate IS NULL OR d.endDate >= :currentTime)")
    Set<Discount> findActiveDiscountsByAppointmentId(Long appointmentId, LocalDateTime currentTime);

    // Все скидки для конкретного человека (PERSON)
    @Query("SELECT d FROM Discount d " +
            "JOIN d.discountUsers u " +
            "WHERE u.id = :userId " +
            "AND d.type = 'PERSON' " +
            "AND d.removed = false " +
            "AND (d.startDate IS NULL OR d.startDate <= :currentTime) " +
            "AND (d.endDate IS NULL OR d.endDate >= :currentTime)")
    Set<Discount> findActiveDiscountsByUserId(Long userId, LocalDateTime currentTime);
}
