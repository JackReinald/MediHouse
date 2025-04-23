package com.antonio.MediHouse.BussinessLogic;

import com.antonio.MediHouse.DTO.MedicineUsageRequest;
import com.antonio.MediHouse.DataAccess.DAMedicine;
import com.antonio.MediHouse.Entities.AlertTypes;
import com.antonio.MediHouse.Entities.Medicine;
import com.antonio.MediHouse.Entities.User;
import com.antonio.MediHouse.ExceptionHandling.ExpiredResourceException;
import com.antonio.MediHouse.ExceptionHandling.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor    // Genera un constructor conforme se añaden más atributos
public class BLMedicine {
    private final DAMedicine medicineDA;
    private final BLUsageHistory usageHistoryBL;
    private final BLAlerts alertsBL;

    // Create
    public Medicine createMedicine(Medicine medicine){
        medicine.setCurrentAmount(Double.valueOf(medicine.getPurchaseAmount()));
        medicine.setRegistrationDate(LocalDateTime.now());
        return medicineDA.save(medicine);
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
    public boolean useMedicine(Long id, MedicineUsageRequest medicineRequest, User user) {
        System.out.println("Entró a logica de negocio de useMedicine()");
        Medicine medicine = getMedicineById(id);    // Verificar que exista el ID
        LocalDate today = LocalDateTime.now().toLocalDate();

        // Verificar estado del stock INICIAL para alertas
        if (medicine.getCurrentAmount() == 0) {
            alertsBL.createAlert(AlertTypes.EMPTY_STOCK, medicine);
        } else if (medicine.getCurrentAmount() <= medicine.getPurchaseAmount() * 0.15) {
            alertsBL.createAlert(AlertTypes.LOW_STOCK, medicine);
        } else if (today.isAfter(medicine.getExpirationDate())) {
            alertsBL.createAlert(AlertTypes.EXPIRED, medicine);
            throw new ExpiredResourceException("The medicine with ID " + id +
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

            return true; // Indica que la operación fue exitosa
        } else {
            // No lanzamos la excepción aquí, simplemente retornamos false
            System.out.println("Not enough stock for medicine with ID: " + id);
            return false; // Indica que la operación falló por falta de stock
        }
    }
    // Delete
    public void deleteMedicine(Long id) throws Exception {
        // It checks if medicine ID exists
        Medicine medicine = getMedicineById(id);
        try {
            medicineDA.deleteById(medicine.getIdMedicine());
        } catch(Exception e){
            throw new Exception("Unexpected error: " + e.getMessage());
        }
    }
}
