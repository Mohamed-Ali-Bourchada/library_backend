package com.miniProjet.libraryProject.Controller;

import com.miniProjet.libraryProject.DTO.UserRegistrationDTO;
import com.miniProjet.libraryProject.Entity.Users;
import com.miniProjet.libraryProject.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Constructor injection for the service
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserRegistrationDTO userDTO) {
        try {
            Users registeredUser = userService.registerUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + ex.getMessage());
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody UserRegistrationDTO userDTO) {
        try {
            Users updatedUser = userService.updateUser(userId, userDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Update failed: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);  // Calls the service to delete the user
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delete failed: " + ex.getMessage());
        }
    }

}
