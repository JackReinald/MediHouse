package com.antonio.MediHouse.DTO;

public record MedicineUsageRequest(
        Double quantityUsed,
        String dosageUnit,
        String reason
) {}
