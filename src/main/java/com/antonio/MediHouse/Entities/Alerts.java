package com.antonio.MediHouse.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Alerts {
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlert;
    // Foreign key
    @ManyToOne
    @JoinColumn(name = "IdMedicine", nullable = false)
    @JsonBackReference
    private Medicine medicine;

    // Entity attributes
    @Column(name = "AlertDate")
    private LocalDateTime alertDate;
    @Column(name = "AlertType")
    @Enumerated(EnumType.STRING)
    private AlertTypes alertType;   // Low stock, empty medicine, expired medicine
    @Column(name = "IsResolved")
    private boolean isResolved = false;

}
