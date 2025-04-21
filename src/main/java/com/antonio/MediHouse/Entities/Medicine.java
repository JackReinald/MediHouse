package com.antonio.MediHouse.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Medicine {
    /// Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMedicine")
    private Long idMedicine;
    // Foreign keys
    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsageHistory> usageHistoryList;
    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alerts> alertList;

    // Entity attributes
    @Column(name = "Name")
    private String name;
    @Column(name = "ActiveIngredient")
    private String activeIngredient;
    @Column(name = "PurchaseAmount")
    private Long purchaseAmount;
    @Column(name = "CurrentAmount")
    private Double currentAmount;
    @Column(name = "RegistrationDate")
    private LocalDateTime registrationDate;
    @Column(name = "ExpirationDate")
    private Date expirationDate;

    // Implicit constructor, getters and setters thanks to Lombok
}
