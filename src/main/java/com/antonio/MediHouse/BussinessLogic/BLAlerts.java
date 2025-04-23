package com.antonio.MediHouse.BussinessLogic;

import com.antonio.MediHouse.DataAccess.DAAlerts;
import com.antonio.MediHouse.Entities.AlertTypes;
import com.antonio.MediHouse.Entities.Alerts;
import com.antonio.MediHouse.Entities.Medicine;
import com.antonio.MediHouse.ExceptionHandling.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BLAlerts {
    private final DAAlerts alertsDA;
    // Create
    public void createAlert(AlertTypes tipoAlerta, Medicine medicine){
        Alerts alert = new Alerts();
        alert.setAlertDate(LocalDateTime.now());
        alert.setAlertType(tipoAlerta);
        alert.setMedicine(medicine);

        alertsDA.save(alert);   // Persistir alerta
    }

    // Read
    public List<Alerts> getAllAlerts(){
        return alertsDA.findAll();
    }
    public Alerts getAlertById(Long id){
        return alertsDA.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert with ID " + id + "not found"));
    }
}
