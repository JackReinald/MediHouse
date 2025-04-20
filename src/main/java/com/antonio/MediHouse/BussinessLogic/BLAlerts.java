package com.antonio.MediHouse.BussinessLogic;

import com.antonio.MediHouse.DataAccess.DAAlerts;
import com.antonio.MediHouse.Entities.Alerts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BLAlerts {
    private final DAAlerts alertsDA;

    public List<Alerts> getAllAlerts(){
        return alertsDA.findAll();
    }
    public Optional<Alerts> getAlertById(Long id){
        return alertsDA.findById(id);
    }
}
