package com.antonio.MediHouse.BussinessLogic;

import com.antonio.MediHouse.DTO.MedicineUsageRequest;
import com.antonio.MediHouse.DataAccess.DAMedicine;
import com.antonio.MediHouse.Entities.AlertTypes;
import com.antonio.MediHouse.Entities.Medicine;
import com.antonio.MediHouse.Entities.User;
import com.antonio.MediHouse.ExceptionHandling.ExpiredResourceException;
import com.antonio.MediHouse.ExceptionHandling.ResourceAlreadyExistsException;
import com.antonio.MediHouse.ExceptionHandling.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor    // Genera un constructor conforme se añaden más atributos
public class BLMedicine {
    private final DAMedicine medicineDA;
    private final BLUsageHistory usageHistoryBL;
    private final BLAlerts alertsBL;

    // Create
    public Medicine createMedicine(Medicine medicine){
      // Verificar que la medicina no esté ya registrada mediante el ingrediente activo y el nombre
      boolean isRegistered =  medicineDA.existsByNameAndActiveIngredient(medicine.getName(), medicine.getActiveIngredient());

      if (!isRegistered) {
        medicine.setCurrentAmount(Double.valueOf(medicine.getPurchaseAmount()));
        medicine.setRegistrationDate(LocalDateTime.now());
        return medicineDA.save(medicine);
      }

      throw new ResourceAlreadyExistsException("The medicine " + medicine.getName() + " with active ingredient " + medicine.getActiveIngredient() + " already exists.");
    }

    // Read
    public List<Medicine> getAllMedicines(){
        return medicineDA.findAll();
    }

    public Medicine getMedicineById(Long id){
        return medicineDA.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine with ID " + id + " was not found"));
    }

    // Update
    public Medicine updateMedicine(Long id, Medicine medicine){
        // It checks if medicine ID exists
        Medicine checkedMedicine = getMedicineById(id);

        medicine.setIdMedicine(checkedMedicine.getIdMedicine());
        return medicineDA.save(medicine);
    }
    @Transactional
    public Map<String, Object> useMedicine(Long id, MedicineUsageRequest medicineRequest, User user) {
        LocalDate today = LocalDateTime.now().toLocalDate();
        Map<String, Object> operation = new HashMap<>();
        boolean isSuccessful = false;
        String message = "";

        try{
            Medicine medicine = getMedicineById(id);    // Verificar que exista el ID


            // Verificar estado del stock INICIAL para alertas
            if (medicine.getCurrentAmount() == 0) {
                alertsBL.createAlert(AlertTypes.EMPTY_STOCK, medicine);
                throw new IllegalArgumentException("Not enough stock for " + medicine.getName()
                        + " (ID= " + id + "), stock is empty" );
            } else if (medicine.getCurrentAmount() <= medicine.getPurchaseAmount() * 0.15) {
                alertsBL.createAlert(AlertTypes.LOW_STOCK, medicine);
            } else if (today.isAfter(medicine.getExpirationDate())) {
                alertsBL.createAlert(AlertTypes.EXPIRED, medicine);
                throw new ExpiredResourceException("The medicine " + medicine.getName()+ "(ID= " + id + ")" +
                        " expired on " + medicine.getExpirationDate());
            }

            // Intentar usar la medicina
            if (medicine.getCurrentAmount() >= medicineRequest.quantityUsed()) {
                var originalAmount = medicine.getCurrentAmount();
                var currentAmount = originalAmount - medicineRequest.quantityUsed();
                medicine.setCurrentAmount(currentAmount);
                medicineDA.save(medicine);  // Persistir operación

                // Verificar si el stock AHORA es cero y crear la alerta
                if (medicine.getCurrentAmount() == 0) {
                    alertsBL.createAlert(AlertTypes.EMPTY_STOCK, medicine);
                }

                usageHistoryBL.recordUsageHistory(medicine, user, medicineRequest);
                isSuccessful = true; // Indica que la operación fue exitosa
            }
        } catch (ResourceNotFoundException | IllegalArgumentException | ExpiredResourceException e){
            message = e.getMessage();
        }
        operation.put("success", isSuccessful);
        operation.put("message", message);

        return operation;
    }
    // Delete
    public Map<String, Object> deleteMedicine(Long id) {
        // It checks if medicine ID exists
        Medicine medicine = getMedicineById(id);
        Map<String, Object> operation = new HashMap<>();
        boolean isSuccessful = false;
        String message = "";
        try {
            medicineDA.deleteById(medicine.getIdMedicine());
            isSuccessful = true;
        } catch(Exception e){
            message = e.getMessage();
        }
        operation.put("isSuccesfull", isSuccessful);
        operation.put("message", message);
        return operation;
    }
}
