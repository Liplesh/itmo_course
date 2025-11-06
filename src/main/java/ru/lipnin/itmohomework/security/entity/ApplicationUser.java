package ru.lipnin.itmohomework.security.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "application_user")
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String username; // логин

    private String password;

    private String email; //Почта, доп информация

    @ManyToOne
    @JoinColumn(name = "role_id")
    private UserRole userRole;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "removed", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean removed;
}
