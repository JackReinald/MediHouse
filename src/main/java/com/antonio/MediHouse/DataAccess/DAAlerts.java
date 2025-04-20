package com.antonio.MediHouse.DataAccess;

import com.antonio.MediHouse.Entities.Alerts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//                                JpaRepository<Entity, ClassOfPrimaryKey>
public interface DAAlerts extends JpaRepository<Alerts, Long> {
}
