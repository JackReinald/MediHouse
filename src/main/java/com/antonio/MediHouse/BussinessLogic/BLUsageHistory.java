package com.antonio.MediHouse.BussinessLogic;

import com.antonio.MediHouse.DataAccess.DAUsageHistory;
import com.antonio.MediHouse.Entities.UsageHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BLUsageHistory {
    private final DAUsageHistory usageHistoryDA;

    public List<UsageHistory> getAllUsageHistory(){
        return usageHistoryDA.findAll();
    }
    public UsageHistory getUsageHistoryById(Long id){
        return usageHistoryDA.getReferenceById(id);
    }
}