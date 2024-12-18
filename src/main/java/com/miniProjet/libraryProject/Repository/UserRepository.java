package com.miniProjet.libraryProject.Repository;

import com.miniProjet.libraryProject.Entity.Users;
import com.miniProjet.libraryProject.Service.UserProjection;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    // Define a method to find a user by email
    Users findByEmail(String email);
    List<UserProjection> findAllProjectedBy();

    
}
