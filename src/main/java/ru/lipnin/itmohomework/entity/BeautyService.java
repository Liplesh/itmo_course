package ru.lipnin.itmohomework.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.lipnin.itmohomework.constants.Category;

import java.time.LocalDateTime;

//Сущность предоставляемой услуги
@Builder
@Getter
@Setter
@Entity
@Table(name = "services")
@NoArgsConstructor
@AllArgsConstructor
public class BeautyService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "price", nullable = false)
    private int price;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "removed", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean removed;
}
