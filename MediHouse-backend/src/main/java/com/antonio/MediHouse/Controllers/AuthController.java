package com.antonio.MediHouse.Controllers;

import com.antonio.MediHouse.BussinessLogic.BLAuth;
import com.antonio.MediHouse.BussinessLogic.BLUser;
import com.antonio.MediHouse.DataAccess.DAUser;
import com.antonio.MediHouse.Entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:9000")
@RequestMapping("api/auth")
public class AuthController {
    private final BLAuth authBL;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        Map<String, Object> operation = authBL.login(email, password);

        return new ResponseEntity<>(operation, HttpStatus.OK);
    }

}
