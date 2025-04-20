package com.antonio.MediHouse.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Medicine medicine;

    // Entity attributes
    @Embedded
    private User user;
    @Column(name = "UsageDate")
    private Date usageDate;
    @Column(name = "DosageUnit")
    private String dosageUnit;
    @Column(name = "UsedAmount")
    private Double usedAmount;
    @Column(name = "Reason")
    private String reason;

}
