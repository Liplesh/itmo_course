package ru.lipnin.itmohomework.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.lipnin.itmohomework.constants.Status;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;

import java.time.LocalDateTime;

//Сущность бронирования
@Builder
@Getter
@Setter
@Entity
@Table(name = "appointment")
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "client_phone", nullable = false)
    private String clientPhone;

    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    @Column(name = "appointment_close")
    private LocalDateTime appointmentClose;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "price")
    private int price;

    @Column(name = "price_note")
    private String priceNote;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "removed", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean removed;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private BeautyService service;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;
}
