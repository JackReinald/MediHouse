package com.antonio.MediHouse.BussinessLogic;

import com.antonio.MediHouse.DataAccess.DAMedicine;
import com.antonio.MediHouse.Entities.Medicine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor    // Genera un constructor con argumentos requeridos
public class BLMedicine {
    private final DAMedicine medicineDA;

    public List<Medicine> getAllMedicines(){
        return medicineDA.findAll();
    }
    public Medicine getMedicineById(Long id){
        return medicineDA.getReferenceById(id);
    }
}
