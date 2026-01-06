package com.antonio.MediHouse.DTO;


public record MedicineUsageDTO(
        MedicineUsageRequestDTO usageRequestDTO,
        Long idUser
) {
}
