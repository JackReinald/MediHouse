package com.antonio.MediHouse.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Medicine medicine;

    // Entity attributes
    @Column(name = "AlertDate")
    private Date alertDate;
    @Column(name = "AlertType")
    private String alertType;
    @Column(name = "IsResolved")
    private boolean isResolved;


}
