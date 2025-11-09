package com.antonio.MediHouse.BussinessLogic;

import com.antonio.MediHouse.DataAccess.DAUser;
import com.antonio.MediHouse.Entities.User;
import com.antonio.MediHouse.ExceptionHandling.ResourceAlreadyExistsException;
import com.antonio.MediHouse.ExceptionHandling.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BLUser {
  private final DAUser userDA;
  // Create
  public User createUser(User user) {
    // Corroborar que el usuario a crear no exista ya mediante correo
    Optional<User> existentUser =  userDA.findByEmail(user.getEmail());
    if (existentUser.isEmpty()) {
      return userDA.save(user);
    }
    throw new ResourceAlreadyExistsException("El correo " + user.getEmail() + " ya existe.");
  }

  // Read
  public User getUserById(Long id){
    return userDA.findById(id).
      orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " was not found"));
  }


}
