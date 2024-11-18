package com.miniProjet.libraryProject.DTO;

import com.miniProjet.libraryProject.Entity.Book;
import com.miniProjet.libraryProject.Entity.Users;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmpreuntRequestDTO {
    private Long id;
    private Users user;
    private Book book;
    private LocalDate dateEmprunt;
    private LocalDate dateRoutourPrevu;
    private LocalDate dateRoutourEffective;
}
