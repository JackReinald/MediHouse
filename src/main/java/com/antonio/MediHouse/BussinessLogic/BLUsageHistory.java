package com.antonio.MediHouse.BussinessLogic;

import com.antonio.MediHouse.DTO.MedicineUsageRequest;
import com.antonio.MediHouse.DataAccess.DAUsageHistory;
import com.antonio.MediHouse.Entities.Medicine;
import com.antonio.MediHouse.Entities.UsageHistory;
import com.antonio.MediHouse.Entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BLUsageHistory {
    private final DAUsageHistory usageHistoryDA;

    public List<UsageHistory> getAllUsageHistory(){
        return usageHistoryDA.findAll();
    }
    public Optional<UsageHistory> getUsageHistoryById(Long id){
        return usageHistoryDA.findById(id);
    }
    public void recordUsageHistory(Medicine medicine, User user, MedicineUsageRequest medicineRequest){
        // Create the record
        UsageHistory usageHistory = new UsageHistory();
        usageHistory.setMedicine(medicine);
        usageHistory.setUser(user);
        usageHistory.setUsageDate(LocalDateTime.now());
        usageHistory.setUsedAmount(medicineRequest.quantityUsed());
        usageHistory.setDosageUnit(medicineRequest.dosageUnit());
        usageHistory.setReason(medicineRequest.reason());

        usageHistoryDA.save(usageHistory);
    }
}