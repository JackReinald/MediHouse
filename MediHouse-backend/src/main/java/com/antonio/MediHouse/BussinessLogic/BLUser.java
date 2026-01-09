package com.antonio.MediHouse.BussinessLogic;

import com.antonio.MediHouse.DataAccess.DAUser;
import com.antonio.MediHouse.Entities.User;
import com.antonio.MediHouse.ExceptionHandling.ResourceAlreadyExistsException;
import com.antonio.MediHouse.ExceptionHandling.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BLUser {
    private final DAUser userDA;

    // Create
    @Transactional
    public Map<String, Object> createUser(User user) {
        Map<String, Object> operation = new HashMap<>();
        // Corroborar que el usuario a crear no exista ya mediante correo
        boolean userExists = userDA.existsByEmail(user.getEmail());
        if (userExists) {
            throw new ResourceAlreadyExistsException("This e-mail is already registered.");
        }
        userDA.save(user);

        operation.put("success", true);
        operation.put("message", "The user " + user.getName() + " " + user.getLastname() + " is now registered.");
        return operation;
    }

    // Read
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userDA.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("The user with ID " + id + " was not found."));
    }


    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userDA.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("The user with email " + email + " was not found."));
    }
}
