package com.miniProjet.libraryProject.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Emprunt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bookId", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    private LocalDate dateEmprunt;
    private LocalDate dateRoutourPrevu;
    private LocalDate dateRoutourEffective;


}
