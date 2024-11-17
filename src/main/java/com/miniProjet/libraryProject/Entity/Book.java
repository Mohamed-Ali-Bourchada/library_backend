package com.miniProjet.libraryProject.Entity;

import com.miniProjet.libraryProject.Enumes.Category;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table (name="Book")
@Data
public class Book {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String sujet;
    private Category category;
    private com.miniProjet.libraryProject.Enumes.StateBook StateBook;
    @Column(name="cover",length = 50000000)
    private byte[] cover;


}
