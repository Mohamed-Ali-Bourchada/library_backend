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
        user.setPassword(encodedPassword); // Set the encoded password

        // Save the user and return
        return userRepository.save(user);
    }

    public Users updateUser(Long userId, UserRegistrationDTO userDTO) {
        Users existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFullName(userDTO.getFullName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setDateNaiss(userDTO.getDateNaiss());
        existingUser.setAdresse(userDTO.getAdresse());
        existingUser.setTelephone(userDTO.getTelephone());

        // If the password is provided, encode and update it
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        return userRepository.save(existingUser);
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
