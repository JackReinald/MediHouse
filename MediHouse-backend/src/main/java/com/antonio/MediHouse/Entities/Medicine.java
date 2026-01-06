package com.antonio.MediHouse.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @JsonManagedReference("medicine-usage")
    @ToString.Exclude
    private List<UsageHistory> usageHistoryList;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("medicine-alerts")
    @ToString.Exclude
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
    private LocalDate expirationDate;

    // Implicit constructor, getters, and setters thanks to Lombok
}
