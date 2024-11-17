package com.miniProjet.libraryProject.Repository;

import com.miniProjet.libraryProject.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
}
