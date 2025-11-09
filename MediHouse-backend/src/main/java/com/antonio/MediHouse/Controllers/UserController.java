package com.antonio.MediHouse.Controllers;

import com.antonio.MediHouse.BussinessLogic.BLUser;
import com.antonio.MediHouse.Entities.User;
import com.antonio.MediHouse.ExceptionHandling.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private final BLUser userBL;

  // Create
  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    User createdUser = userBL.createUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  // Read
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userBL.getUserById(id));
  }

  // Manejo de excepción recurso existente
  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> resourceAlreadyExistsException(Exception ex) {
    Map<String, Object> error = new HashMap<>();
    error.put("error", "El usuario ya existe ");
    error.put("message", ex.getMessage());
    error.put("status", HttpStatus.CONFLICT);

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  // Manejo de cualquier Null Pointer
  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<Map<String, Object>> nullPointerException(Exception ex) {
    Map<String, Object> error = new HashMap<>();
    error.put("error", "El usuario ya existe ");
    error.put("message", ex.getMessage());
    error.put("status", HttpStatus.CONFLICT);

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }
}
