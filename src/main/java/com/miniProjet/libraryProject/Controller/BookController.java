package com.miniProjet.libraryProject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniProjet.libraryProject.DTO.BookRequestDTO;
import com.miniProjet.libraryProject.Entity.Book;
import com.miniProjet.libraryProject.Entity.Users;
import com.miniProjet.libraryProject.Enumes.Category;
import com.miniProjet.libraryProject.Enumes.StateBook;
import com.miniProjet.libraryProject.Repository.BookRepository;
import com.miniProjet.libraryProject.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

@Controller
@RequestMapping("/api/book")
@CrossOrigin("*")
public class BookController {
    @Autowired
    public BookService bookService;

    @GetMapping("/")
    public ResponseEntity<?> allBooks() throws DataFormatException, IOException {
        try {
            List<Book> allBooks = bookService.GetAllBooks();
            return new ResponseEntity<>(allBooks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/createBook")
    public ResponseEntity<?> createBook(@RequestParam("bookDTO") String bookDTO,
            @RequestParam("cover") MultipartFile cover) {
        try {
            // Define the maximum allowed file size (e.g., 2MB)
            long maxSizeInBytes = 2 * 1024 * 1024; // 2 MB in bytes

            // Check if the cover file exceeds the maximum allowed size
            if (cover.getSize() > maxSizeInBytes) {
                System.out.println("File size is too large: " + cover.getSize() + " bytes");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error",
                                "La taille du fichier dépasse la limite autorisée de 2 MB. Veuillez télécharger un fichier plus petit."));
            }

            // Deserialize the bookDTO JSON into a BookRequestDTO object
            ObjectMapper objectMapper = new ObjectMapper();
            BookRequestDTO bookRequest = objectMapper.readValue(bookDTO, BookRequestDTO.class);

            // Call the service to create the book
            bookService.CreateBook(bookRequest, cover);

            // Return a success response as JSON
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Livre créé avec succès"));

        } catch (Exception e) {
            // Return an error response with details
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("getBooksByCategorie/{categorie}")
    public ResponseEntity<?> getBooksByCategorie(@PathVariable("categorie") String categorie) {
        try{
            List<Book> booksByCategorie = bookService.getBooksByCategorie(Category.valueOf(categorie));
            return new ResponseEntity<>(booksByCategorie, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("getBooksByState/{disponible}")
    public ResponseEntity<?> getBooksByState(@PathVariable("disponible") String disponible) {
        try {
            List<Book> bookByState=bookService.getBooksByState(StateBook.valueOf(disponible));
            return new ResponseEntity<>(bookByState, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{title}")
    public ResponseEntity<?> getBooksByTitle(@PathVariable("title") String title) {
        try {
            List<Book> bookByState=bookService.getBooksByTitle(title);
            return new ResponseEntity<>(bookByState, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("{id}/update")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookRequestDTO bookDto) {
        try {
            // Attempt to update the book in the database
            bookService.UpdateBook(id, bookDto);
            // Return success response if update is successful
            return ResponseEntity.ok("Livre mis à jour avec succès !");
        } catch (IllegalArgumentException e) {
            // If validation fails, return a BAD_REQUEST error with the exception message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Les données du livre sont invalides : " + e.getMessage());
        } catch (Exception e) {
            // General error catch
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour du livre : " + e.getMessage());
        }
    }

    @PutMapping("{id}/updateCover")
    public ResponseEntity<?> updateCover(@PathVariable Long id, @RequestParam("cover") MultipartFile cover) {
        try {
            bookService.UpdateCoverBook(id, cover);
            return new ResponseEntity("update cover success !", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/deleteBooks")
    public ResponseEntity<Map<String, Object>> deleteBooks(@RequestBody List<Long> bookIds) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Ensure that the books are actually deleted
            bookService.deleteBooks(bookIds);

            // Return success response if delete is successful
            response.put("success", true);
            response.put("message", "Livres supprimés avec succès !");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // If validation fails (e.g., invalid book IDs), return a BAD_REQUEST error
            response.put("success", false);
            response.put("message", "Les identifiants des livres sont invalides : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            // General error catch (for database issues, etc.)
            response.put("success", false);
            response.put("message", "Erreur lors de la suppression des livres : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // get all books
    // delet book
}
