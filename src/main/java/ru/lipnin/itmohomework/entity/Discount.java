package ru.lipnin.itmohomework.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.lipnin.itmohomework.constants.DiscountType;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discont")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Float value;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private DiscountType type;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    // Для type = SERVICE
    @ManyToMany
    @JoinTable(
            name = "discount_services",
            joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<BeautyService> discountServices = new HashSet<>();

    // Для type = APPOINTMENT
    @ManyToMany
    @JoinTable(
            name = "discount_appointments",
            joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "appoitment_id")
    )
    private Set<Appointment> discountAppointments = new HashSet<>();

    // Для type = PERSON
    @ManyToMany
    @JoinTable(
            name = "discount_users",
            joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<ApplicationUser> discountUsers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "removed", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean removed;

}
