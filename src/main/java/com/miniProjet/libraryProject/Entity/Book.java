package com.miniProjet.libraryProject.Entity;

import com.miniProjet.libraryProject.Enumes.Category;
import com.miniProjet.libraryProject.Enumes.StateBook;
import com.miniProjet.libraryProject.Enumes.StateBook;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Book")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String sujet;
    private Category category;
    private StateBook stateBook;
    @Column(name = "cover", length = 50000000)
    private byte[] cover;

}
