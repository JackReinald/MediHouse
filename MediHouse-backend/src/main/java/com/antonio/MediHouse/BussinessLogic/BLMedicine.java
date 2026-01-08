package com.antonio.MediHouse.BussinessLogic;

import com.antonio.MediHouse.DTO.MedicineUsageRequestDTO;
import com.antonio.MediHouse.DataAccess.DAMedicine;
import com.antonio.MediHouse.Entities.AlertTypes;
import com.antonio.MediHouse.Entities.Medicine;
import com.antonio.MediHouse.Entities.User;
import com.antonio.MediHouse.ExceptionHandling.ExpiredResourceException;
import com.antonio.MediHouse.ExceptionHandling.ResourceAlreadyExistsException;
import com.antonio.MediHouse.ExceptionHandling.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor    // Genera un constructor conforme se añaden más atributos
public class BLMedicine {
    private final DAMedicine medicineDA;
    private final BLUsageHistory usageHistoryBL;
    private final BLAlerts alertsBL;
    private final BLUser userBL;

    // Create
    @Transactional
    public Medicine createMedicine(Medicine medicine) {
        // Verificar que la medicina no esté ya registrada mediante el ingrediente activo y el nombre
        boolean isRegistered = medicineDA.existsByNameAndActiveIngredient(medicine.getName(), medicine.getActiveIngredient());

        if (!isRegistered) {
            medicine.setCurrentAmount(Double.valueOf(medicine.getPurchaseAmount()));
            medicine.setRegistrationDate(LocalDateTime.now());
            return medicineDA.save(medicine);
        }

        if (medicine.getCurrentAmount() > medicine.getPurchaseAmount()) {
            throw new RuntimeException("The current amount must be lower than the purchase amount");
        }

        throw new ResourceAlreadyExistsException("The medicine " + medicine.getName() + " with active ingredient " + medicine.getActiveIngredient() + " already exists.");
    }


    // Read
    @Transactional(readOnly = true)
    public List<Medicine> getAllMedicines() {
        return medicineDA.findAll();
    }

    @Transactional(readOnly = true)
    public Medicine getMedicineById(Long id) {
        return medicineDA.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine with ID " + id + " was not found"));
    }

    // Update
    @Transactional
    public Medicine updateMedicine(Long id, Medicine medicine) {
        // It checks if medicine ID exists
        Medicine checkedMedicine = getMedicineById(id);

        medicine.setIdMedicine(checkedMedicine.getIdMedicine());
        return medicineDA.save(medicine);
    }

    @Transactional
    public Map<String, Object> useMedicine(Long id, Long idUser, MedicineUsageRequestDTO medicineRequest) {
        LocalDate today = LocalDateTime.now().toLocalDate();
        Map<String, Object> operation = new HashMap<>();
        Medicine medicine = getMedicineById(id);    // Verificar que exista el ID
        User user = userBL.getUserById(idUser);



        // Verificar estado del stock INICIAL para alertas
        if (medicine.getCurrentAmount() == 0) {
            alertsBL.createAlert(AlertTypes.EMPTY_STOCK, medicine);
            throw new IllegalArgumentException("Not enough stock for " + medicine.getName()
                    + " (ID= " + id + "), stock is empty");
        } else if (today.isAfter(medicine.getExpirationDate())) {
            alertsBL.createAlert(AlertTypes.EXPIRED, medicine);
            throw new ExpiredResourceException("The medicine " + medicine.getName() + "(ID= " + id + ")" +
                    " expired on " + medicine.getExpirationDate());
        } else if (medicine.getCurrentAmount() < medicineRequest.quantityUsed()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock");
        }

        // Lógica de éxito (Solo se ejecuta si no saltaron excepciones arriba)
        if (medicine.getCurrentAmount() <= medicine.getPurchaseAmount() * 0.15) {
            alertsBL.createAlert(AlertTypes.LOW_STOCK, medicine);
        }

        medicine.setCurrentAmount(medicine.getCurrentAmount() - medicineRequest.quantityUsed());
        usageHistoryBL.recordUsageHistory(medicine, user, medicineRequest);
        medicineDA.save(medicine);  // Persiste la operación

        if (medicine.getCurrentAmount() == 0) {
            alertsBL.createAlert(AlertTypes.EMPTY_STOCK, medicine);
        }

        operation.put("success", true);
        operation.put("message", "Medicine used successfully");
        operation.put("reaminingAmout", medicine.getCurrentAmount());
        return operation;
    }

    // Delete
    @Transactional
    public Map<String, Object> deleteMedicine(Long id) {
        // It checks if medicine ID exists
        Medicine medicine = getMedicineById(id);
        medicineDA.deleteById(medicine.getIdMedicine());

        Map<String, Object> operation = new HashMap<>();
        operation.put("success", true);
        operation.put("message", "Medicine deleted successfully");
        return operation;
    }


}
