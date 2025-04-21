package com.antonio.MediHouse.BussinessLogic;

import com.antonio.MediHouse.DTO.MedicineUsageRequest;
import com.antonio.MediHouse.DataAccess.DAMedicine;
import com.antonio.MediHouse.Entities.Medicine;
import com.antonio.MediHouse.Entities.User;
import com.antonio.MediHouse.ExceptionHandling.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor    // Genera un constructor conforme se añaden más atributos
public class BLMedicine {
    private final DAMedicine medicineDA;
    private final BLUsageHistory usageHistoryBL;

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
    public Medicine useMedicine(Long id, MedicineUsageRequest medicineRequest, User user){
        System.out.println("Entró a logica de negocio de useMedicine()");
        Medicine medicine = getMedicineById(id);    // Verificar que exista el ID

        // Verificar si la cantidad usada de medicina supera la actual
        if (medicine.getCurrentAmount() >= medicineRequest.quantityUsed()) {
            // Obtener cantidad de existencias actual
            var originalAmount = medicine.getCurrentAmount();
            // Restar la cantidad usada a la cantidad original para obtener la actual
            var currentAmount = originalAmount - medicineRequest.quantityUsed();
            // Guardar la nueva cantidad
            medicine.setCurrentAmount(currentAmount);
            medicineDA.save(medicine);  // Persistir operación
            // Persistir la operación en UsageHistory
            usageHistoryBL.recordUsageHistory(medicine, user, medicineRequest);
            return medicine;
        }
        else {
            throw new IllegalArgumentException("Not enough stock for medicine with ID: " + id);
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
