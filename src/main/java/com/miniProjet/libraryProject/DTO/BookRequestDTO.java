package com.miniProjet.libraryProject.DTO;

import com.miniProjet.libraryProject.Enumes.Category;
import com.miniProjet.libraryProject.Enumes.StateBook;
import lombok.Data;

@Data
public class BookRequestDTO {
    private Long id;
    private String title;
    private String author;
    private String sujet;
    private Category category;
    private StateBook stateBook;
    private byte[] cover;
}
