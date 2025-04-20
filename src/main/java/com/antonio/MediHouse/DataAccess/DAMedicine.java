package com.antonio.MediHouse.DataAccess;

import com.antonio.MediHouse.Entities.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//                                  JpaRepository<Entity, ClassOfPrimaryKey>
public interface DAMedicine extends JpaRepository<Medicine, Long> {
}
