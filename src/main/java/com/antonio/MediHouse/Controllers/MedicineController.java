package com.antonio.MediHouse.Controllers;

import com.antonio.MediHouse.BussinessLogic.BLMedicine;
import com.antonio.MediHouse.DTO.MedicineUsageRequest;
import com.antonio.MediHouse.Entities.Medicine;
import com.antonio.MediHouse.Entities.User;
import com.antonio.MediHouse.ExceptionHandling.ResourceAlreadyExistsException;
import com.antonio.MediHouse.ExceptionHandling.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
                                         @RequestBody MedicineUsageRequest medicineRequest,
                                         @RequestBody User user){

        Map<String, Object> operation = medicineBL.useMedicine(id, medicineRequest, user);

        if ((boolean) operation.get("success")) {
            return ResponseEntity.ok(operation);
        } else {
            String tipoError = operation.get("message").toString().toLowerCase();
            if (tipoError.contains("not found")){
                return new ResponseEntity<>(operation, HttpStatus.NOT_FOUND);
            } else if (tipoError.contains("empty")) {
                return new ResponseEntity<>(operation, HttpStatus.BAD_REQUEST);
            } else if (tipoError.contains("expired")) {
                return new ResponseEntity<>(operation, HttpStatus.FORBIDDEN);
            } else {
                return ResponseEntity.badRequest().body(operation);
            }

        }
    }

    // Delete
    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<?> deleteMedicine(@PathVariable Long id){
        var operation = medicineBL.deleteMedicine(id);

        try{
            return new ResponseEntity<>(operation,HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(operation, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(operation, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(operation, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

  // Manejo de excepción recurso existente
  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> resourceAlreadyExistsException(Exception ex) {
    Map<String, Object> error = new HashMap<>();
    error.put("error", "The medicine already existe ");
    error.put("message", ex.getMessage());
    error.put("status", HttpStatus.CONFLICT);

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }
}
