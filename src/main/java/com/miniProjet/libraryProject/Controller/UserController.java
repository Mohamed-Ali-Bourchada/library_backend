package com.miniProjet.libraryProject.Controller;

import com.miniProjet.libraryProject.DTO.UserRegistrationDTO;
import com.miniProjet.libraryProject.Entity.Users;
import com.miniProjet.libraryProject.Repository.UserRepository;
import com.miniProjet.libraryProject.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
@GetMapping("/{userId}/profile")
    public ResponseEntity<Object> getUserProfile(@PathVariable Long userId) {
        try {
            Users user = userService.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user profile: " + ex.getMessage());
        }
    }

    // PUT: Update user profile by userId
   @PutMapping("/{userId}/update")
public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody UserRegistrationDTO userDTO) {
    try {
        // Fetch the user
        Users existingUser = userService.findById(userId);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Hash the password before saving it
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userDTO.getPassword());

        // Update the user's details
        existingUser.setFullName(userDTO.getFullName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPassword(hashedPassword);  // Save the hashed password
        existingUser.setDateNaiss(userDTO.getDateNaiss());
        existingUser.setAdresse(userDTO.getAdresse());
        existingUser.setTelephone(userDTO.getTelephone());

        // Save the updated user
        Users updatedUser = userService.updateUser(existingUser);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed: " + ex.getMessage());
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
    @Autowired
    private UserRepository userRepository;

    // Endpoint to get details of the currently authenticated user
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        // Get the username (email) from the authenticated user
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        // Fetch the user details from the database
        Users dbUser = userRepository.findByEmail(user.getUsername());

        // Check if user exists
        if (dbUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        // Build the response map with user details
        Map<String, Object> response = new HashMap<>();
        response.put("user", dbUser.getEmail());
        response.put("isAdmin", dbUser.getIsAdmin());
        response.put("fullName", dbUser.getFullName());
        response.put("id", dbUser.getId());

        // Return the response
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

