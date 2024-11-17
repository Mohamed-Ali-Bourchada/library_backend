package com.miniProjet.libraryProject.DTO;

import com.miniProjet.libraryProject.Enumes.Category;
import lombok.Data;

@Data
public class BookRequestDTO {
    private Long id;
    private String title;
    private String author;
    private String sujet;
    private Category category;
    private com.miniProjet.libraryProject.Enumes.StateBook StateBook;
    private byte[] cover;
}
