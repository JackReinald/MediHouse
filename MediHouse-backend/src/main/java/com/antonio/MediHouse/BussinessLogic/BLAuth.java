package com.antonio.MediHouse.BussinessLogic;

import com.antonio.MediHouse.Entities.User;
import com.antonio.MediHouse.ExceptionHandling.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BLAuth {
    private final BLUser userBL;

    @Transactional
    public Map<String, Object> login(String email, String password) {
        Map<String, Object> operation = new HashMap<>();

        // Buscar el usuario
        User user = userBL.getUserByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
           throw new ResourceNotFoundException("You typed the wrong password or email, try again");
        }
        operation.put("success", true);
        operation.put("message", "welcome " + user.getName() + " to MediHouse");
        operation.put("user", user);
        return operation;
    }
}
