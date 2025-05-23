package com.antonio.MediHouse.Controllers;


import com.antonio.MediHouse.BussinessLogic.BLAlerts;
import com.antonio.MediHouse.Entities.Alerts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alerts")
public class AlertsController {
    private final BLAlerts alertsBL;

    // Read
    @GetMapping({"", "/"})
    public ResponseEntity<List<Alerts>> getAllAlerts(){
        List<Alerts> alertsList = alertsBL.getAllAlerts();
        return new ResponseEntity<>(alertsList, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Alerts> getAlertById(@PathVariable Long id){
        Alerts alerts = alertsBL.getAlertById(id);
        return ResponseEntity.ok(alerts);
    }

}


