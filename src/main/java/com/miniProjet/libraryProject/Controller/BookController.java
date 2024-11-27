package com.miniProjet.libraryProject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniProjet.libraryProject.DTO.BookRequestDTO;
import com.miniProjet.libraryProject.Entity.Book;
import com.miniProjet.libraryProject.Entity.Users;
import com.miniProjet.libraryProject.Repository.BookRepository;
import com.miniProjet.libraryProject.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

@Controller
@RequestMapping("/api/book")
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
    public ResponseEntity<?> CreateUser(@RequestParam("bookDTO") String bookDTO,
            @RequestParam("cover") MultipartFile cover) throws DataFormatException, IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BookRequestDTO bookRequest = objectMapper.readValue(bookDTO, BookRequestDTO.class); // Deserializes the JSON
                                                                                                // string

            bookService.CreateBook(bookRequest, cover); // Passes the deserialized object and file
            return new ResponseEntity<>("created", HttpStatus.CREATED);
            // return ResponseEntity.status(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("{id}/update")
    public ResponseEntity<?> updteBook(@PathVariable Long id, @RequestBody BookRequestDTO bookTdo) {
        try {
            Book bookToUpdate = bookService.UpdateBook(id, bookTdo);
            return new ResponseEntity("update success !", HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return new ResponseEntity("delete success !", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    // get all books
    // delet book
}
