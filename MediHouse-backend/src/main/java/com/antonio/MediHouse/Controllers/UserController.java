package com.antonio.MediHouse.Controllers;

import com.antonio.MediHouse.BussinessLogic.BLUser;
import com.antonio.MediHouse.Entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:9000")
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
  @GetMapping({"/{id}", "/{id}/"})
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userBL.getUserById(id));
  }
}
