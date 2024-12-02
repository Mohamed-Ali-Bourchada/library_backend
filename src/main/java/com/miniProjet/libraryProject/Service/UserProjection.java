package com.miniProjet.libraryProject.Service;

import java.time.LocalDate;

public interface UserProjection {
        Long getId();
        String getFullName();

        String getEmail();

        String getTelephone();

        String getAdresse();

        LocalDate getDateNaiss();

        LocalDate getDateInscri();
        Boolean getIsAdmin();
    }

