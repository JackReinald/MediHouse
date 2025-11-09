package com.antonio.MediHouse.DataAccess;

import com.antonio.MediHouse.Entities.UsageHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//                                      JpaRepository<Entity, ClassOfPrimaryKey>
public interface DAUsageHistory extends JpaRepository<UsageHistory, Long> {
}
