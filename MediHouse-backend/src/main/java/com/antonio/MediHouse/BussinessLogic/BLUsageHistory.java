package com.antonio.MediHouse.BussinessLogic;

import com.antonio.MediHouse.DTO.MedicineUsageRequestDTO;
import com.antonio.MediHouse.DataAccess.DAUsageHistory;
import com.antonio.MediHouse.Entities.Medicine;
import com.antonio.MediHouse.Entities.UsageHistory;
import com.antonio.MediHouse.Entities.User;
import com.antonio.MediHouse.ExceptionHandling.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BLUsageHistory {
    private final DAUsageHistory usageHistoryDA;

    // Create
    @Transactional
    public void recordUsageHistory(Medicine medicine, User user, MedicineUsageRequestDTO medicineRequest){
        // Create the record
        UsageHistory usageHistory = new UsageHistory();
        usageHistory.setMedicine(medicine);
        usageHistory.setMedicineName(medicine.getName());
        usageHistory.setUser(user);
        usageHistory.setUsageDate(LocalDateTime.now());
        usageHistory.setUsedAmount(medicineRequest.quantityUsed());
        usageHistory.setDosageUnit(medicineRequest.dosageUnit());
        usageHistory.setReason(medicineRequest.reason());

        usageHistoryDA.save(usageHistory);
    }

    // Read
    @Transactional(readOnly = true)
    public List<UsageHistory> getAllUsageHistory(){
        return usageHistoryDA.findAll();
    }

    @Transactional(readOnly = true)
    public UsageHistory getUsageHistoryById(Long id) {
        // findById busca inmediatamente y lanzará tu excepción si no existe
        return usageHistoryDA.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usage history with ID " + id + " was not found"));
    }
}