package com.miniProjet.libraryProject.Service;

import com.miniProjet.libraryProject.DTO.UserRegistrationDTO;
import com.miniProjet.libraryProject.Entity.Users;
import com.miniProjet.libraryProject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Users registerUser(UserRegistrationDTO userDTO) {
        // Check if the email is already registered
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("Email is already registered!.");
        }

        // Create a new user from DTO
        Users user = new Users();
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setDateNaiss(userDTO.getDateNaiss());
        user.setAdresse(userDTO.getAdresse());
        user.setTelephone(userDTO.getTelephone());
        user.setDateInscri(LocalDate.now());

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

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

}
