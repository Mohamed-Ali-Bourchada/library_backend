package com.miniProjet.libraryProject.Service;

import com.miniProjet.libraryProject.DTO.UserRegistrationDTO;
import com.miniProjet.libraryProject.Entity.Users;
import com.miniProjet.libraryProject.Exception.EmailAlreadyExistsException;
import com.miniProjet.libraryProject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users registerUser(UserRegistrationDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
    throw new EmailAlreadyExistsException("Email is already registered.");
    }


        // Encode the password before setting it
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // Create a new user from DTO
        Users user = new Users();
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setDateNaiss(userDTO.getDateNaiss());
        user.setAdresse(userDTO.getAdresse());
        user.setTelephone(userDTO.getTelephone());
        user.setDateInscri(LocalDate.now());
        // Set the encoded password

        user.setPassword(encodedPassword); 
        user.setIsAdmin(false);

        // Save the user and return
        return userRepository.save(user);
    }

    public Users findById(Long userId) {
        return userRepository.findById(userId).orElse(null); // Return null if not found
    }

    // Update a user in the database
    public Users updateUser(Users updatedUser) {
        return userRepository.save(updatedUser);
    }

    public void deleteUser(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    // Method to find a user by email
    public Users getUserByEmail(String email) {
        Users user = userRepository.findByEmail(email);  // Correct method usage
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return user;
    }
}
