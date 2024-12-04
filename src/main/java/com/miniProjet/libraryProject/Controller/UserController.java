package com.miniProjet.libraryProject.Controller;

import com.miniProjet.libraryProject.DTO.ChangePasswordRequest;
import com.miniProjet.libraryProject.DTO.UserRegistrationDTO;
import com.miniProjet.libraryProject.Entity.Users;
import com.miniProjet.libraryProject.Repository.UserRepository;
import com.miniProjet.libraryProject.Service.UserProjection;
import com.miniProjet.libraryProject.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
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

    @GetMapping("/getAllUsers")
    public List<UserProjection> getAllUsers() {
        return userService.getAllUsers();
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


        // Update the user's details
        existingUser.setFullName(userDTO.getFullName());
        existingUser.setEmail(userDTO.getEmail());
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

@Autowired
private PasswordEncoder passwordEncoder;

@PutMapping("/change-password")
@CrossOrigin(origins = "http://localhost:4200")
public ResponseEntity<Map<String, String>> changePassword(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestBody ChangePasswordRequest changePasswordRequest) {

    try {
        // Log the input
        System.out.println("Authorization Header: " + authorizationHeader);
        System.out.println("ChangePasswordRequest: " + changePasswordRequest);

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String encodedCredentials = authorizationHeader.substring(6); // Remove "Basic "
            String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials));
            String[] credentials = decodedCredentials.split(":");

            String username = credentials[0];
            String password = credentials[1];

            // Debug: log decoded credentials
            System.out.println("Decoded Username: " + username);
            System.out.println("Decoded Password: " + password);

            // Fetch user by username (email in this case)
            Users user = userRepository.findByEmail(username);
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                // Check if the current password matches the one in the request
                if (passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
                    // Proceed to change the password
                    user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                    userRepository.save(user);

                    // Return a structured JSON response
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Password updated successfully");

                    return ResponseEntity.ok(response); // Return a 200 OK with the message
                } else {
                    // Return an error message if current password is incorrect
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Current password is incorrect");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            } else {
                // Return an unauthorized error if the credentials are invalid
                Map<String, String> response = new HashMap<>();
                response.put("message", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } else {
            // Return an unauthorized error if authorization header is missing or invalid
            Map<String, String> response = new HashMap<>();
            response.put("message", "Authorization header missing or invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    } catch (Exception e) {
        // Log the exception and return a generic error message
        e.printStackTrace();
        Map<String, String> response = new HashMap<>();
        response.put("message", "An error occurred while processing the request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}


@CrossOrigin(origins = "http://localhost:4200")
@DeleteMapping("/{userId}/delete")
public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
    try {
        userService.deleteUser(userId); // Calls the service to delete the user
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete failed: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

    @Autowired
    private UserRepository userRepository;

    // Endpoint to get details of the currently authenticated user
    @PostMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        // Get the username (email) from the authenticated user
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication
                .getPrincipal();

        // Fetch the user details from the database
        Users dbUser = userRepository.findByEmail(user.getUsername());

        // Check if the user exists
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

        // Return the response without any additional headers
        return ResponseEntity.ok()
                .body(response);
    }

}

