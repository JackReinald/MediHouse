package com.antonio.MediHouse.Controllers;

import com.antonio.MediHouse.BussinessLogic.BLUsageHistory;
import com.antonio.MediHouse.Entities.Medicine;
import com.antonio.MediHouse.Entities.UsageHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class UsageHistoryController {
    private final BLUsageHistory usageHistoryBL;

    @GetMapping({"", "/"})
    public ResponseEntity<List<UsageHistory>> getAllMedicines(){
        List<UsageHistory> historyList = usageHistoryBL.getAllUsageHistory();
        return new ResponseEntity<>(historyList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsageHistory> getMedicineById(@PathVariable Long id) {
        Optional<UsageHistory> history = usageHistoryBL.getUsageHistoryById(id);
        return history.map(h -> new ResponseEntity<>(h, HttpStatus.OK))
                .orElseThrow(() -> new NoSuchElementException("Alert not found with ID: " + id ));
    }
}
