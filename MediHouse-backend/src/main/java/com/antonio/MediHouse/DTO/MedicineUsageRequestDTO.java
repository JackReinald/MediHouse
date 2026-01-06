package com.antonio.MediHouse.DTO;

public record MedicineUsageRequestDTO(
        Double quantityUsed,
        String dosageUnit,
        String reason
) {}
