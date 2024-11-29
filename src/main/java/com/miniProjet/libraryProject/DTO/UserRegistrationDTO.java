package com.miniProjet.libraryProject.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRegistrationDTO {
    private String fullName;
    private String email;
    private String password;
    private LocalDate dateNaiss;
    private String adresse;
    private String telephone;
    private boolean isAdmin = false;
}
