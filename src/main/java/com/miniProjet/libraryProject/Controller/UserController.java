package com.miniProjet.libraryProject.Controller;

import com.miniProjet.libraryProject.DTO.UserRegistrationDTO;
import com.miniProjet.libraryProject.Entity.Users;
import com.miniProjet.libraryProject.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
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

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserRegistrationDTO userDTO) {
        try {
            Users registeredUser = userService.registerUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + ex.getMessage());
        }
    }

    // Endpoint for updating user details
    @PutMapping("/{userId}/update")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody UserRegistrationDTO userDTO) {
        try {
            Users updatedUser = userService.updateUser(userId, userDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Update failed: " + ex.getMessage());
        }
    }

    // Endpoint for deleting a user
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId); // Calls the service to delete the user
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delete failed: " + ex.getMessage());
        }
    }

    // New endpoint to get details of the currently authenticated user
    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(Authentication authentication) {
        // Fetch the currently authenticated user from the Authentication object
        User user = (User) authentication.getPrincipal();

        // Return the email or other user details
        return ResponseEntity.status(HttpStatus.OK).body(user.getUsername());
    }
}
