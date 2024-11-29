package com.miniProjet.libraryProject.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private LocalDate dateNaiss;

    private String adresse;

    private String telephone;

    private LocalDate dateInscri;

    private String password;
    private Boolean isAdmin = false;

}
