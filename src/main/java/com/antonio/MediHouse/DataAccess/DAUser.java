package com.antonio.MediHouse.DataAccess;

import com.antonio.MediHouse.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DAUser extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

}
