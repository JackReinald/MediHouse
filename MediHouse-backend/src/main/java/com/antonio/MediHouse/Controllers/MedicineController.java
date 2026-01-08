package com.antonio.MediHouse.Controllers;

import com.antonio.MediHouse.BussinessLogic.BLMedicine;
import com.antonio.MediHouse.DTO.MedicineUsageDTO;
import com.antonio.MediHouse.DTO.MedicineUsageRequestDTO;
import com.antonio.MediHouse.Entities.Medicine;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:9000")
@RequestMapping("/api/medicine")
public class MedicineController {
    private final BLMedicine medicineBL;

    // Create
    @PostMapping
    public ResponseEntity<Medicine> createMedicine(@RequestBody Medicine medicine){
        Medicine newMedicine = medicineBL.createMedicine(medicine);
        return new ResponseEntity<>(newMedicine, HttpStatus.CREATED);
    }

    // Read
    @GetMapping({"", "/"})
    public ResponseEntity<List<Medicine>> getAllMedicines(){
        List<Medicine> medicineList = medicineBL.getAllMedicines();
        return new ResponseEntity<>(medicineList, HttpStatus.OK);
    }
    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        Medicine medicine = medicineBL.getMedicineById(id);
        return ResponseEntity.ok(medicine);
    }

    // Update
    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine){
        Medicine updatedMedicine = medicineBL.updateMedicine(id, medicine);
        return new ResponseEntity<>(updatedMedicine, HttpStatus.OK);
    }
    @PutMapping({"/{idMedicine}/use", "/{idMedicine}/use/"})
    public ResponseEntity<Map<String, Object>> useMedicine(@PathVariable Long idMedicine,
                                         @RequestBody MedicineUsageDTO wrapperDTO){
        MedicineUsageRequestDTO medicineRequest = wrapperDTO.usageRequestDTO();
        Long idUser = wrapperDTO.idUser();
        Map<String, Object> operation = medicineBL.useMedicine(idMedicine, idUser, medicineRequest);

        return new ResponseEntity<>(operation, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<?> deleteMedicine(@PathVariable Long id){
        var operation = medicineBL.deleteMedicine(id);
        return new ResponseEntity<>(operation, HttpStatus.OK);
    }
}
