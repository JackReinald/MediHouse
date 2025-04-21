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
    @GetMapping("/{id}")
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        Medicine medicine = medicineBL.getMedicineById(id);
        return ResponseEntity.ok(medicine);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine){
        Medicine updatedMedicine = medicineBL.updateMedicine(id, medicine);
        return new ResponseEntity<>(updatedMedicine, HttpStatus.OK);
    }
    @PutMapping("/{id}/use")
    public ResponseEntity<Medicine> useMedicine(@PathVariable Long id,
                                                @RequestParam String userName,
                                                @RequestParam int userAge,
                                                @RequestBody MedicineUsageRequest medicineRequest){
        try {
            System.out.println("Entró en el try");
            User user = new User(userName, userAge);
            Medicine usedMedicine = medicineBL.useMedicine(id,medicineRequest, user);
            return ResponseEntity.ok(usedMedicine);
        } catch (ResourceNotFoundException e){
            System.out.println("Entró en resource not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            System.out.println("Entró en bad request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            System.out.println("Entró en error 500");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete
    @DeleteMapping("/{id}")
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
