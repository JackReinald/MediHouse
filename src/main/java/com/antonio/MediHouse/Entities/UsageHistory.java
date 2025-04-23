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
public class UsageHistory {
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUsage")
    private Long idUsage;
    // Foreign key
    @ManyToOne
    @JoinColumn(name = "IdMedicine", nullable = false)
    @JsonBackReference
    private Medicine medicine;

    // Entity attributes
    @Column(name = "MedicineName")
    private String medicineName;
    @Embedded
    private User user;
    @Column(name = "UsageDate")
    private LocalDateTime usageDate;
    @Column(name = "DosageUnit")
    private String dosageUnit;  // Modificar luego con un enum con todas las posibles unidades
    @Column(name = "UsedAmount")
    private Double usedAmount;
    @Column(name = "Reason")
    private String reason;

}
