package com.antonio.MediHouse.Controllers;

import com.antonio.MediHouse.BussinessLogic.BLMedicine;
import com.antonio.MediHouse.DTO.MedicineUsageRequest;
import com.antonio.MediHouse.Entities.Medicine;
import com.antonio.MediHouse.Entities.User;
import com.antonio.MediHouse.ExceptionHandling.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;


@RestController
@RequiredArgsConstructor
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
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
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
    @PutMapping({"/{id}/use", "/{id}/use/"})
    public ResponseEntity<?> useMedicine(@PathVariable Long id,
                                                @RequestParam String userName,
                                                @RequestParam int userAge,
                                                @RequestBody MedicineUsageRequest medicineRequest){
        User user = new User(userName, userAge);
        boolean success = medicineBL.useMedicine(id, medicineRequest, user);
        if (success) {
            Medicine updatedMedicine = medicineBL.getMedicineById(id); // Recargar la medicina actualizada
            return ResponseEntity.ok(updatedMedicine);
        } else {
            return ResponseEntity.badRequest().body("Not enough stock for medicine with ID: " + id);
        }
    }

    // Delete
    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<String> deleteMedicine(@PathVariable Long id){
        try{
            medicineBL.deleteMedicine(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
