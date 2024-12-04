package com.miniProjet.libraryProject.Controller;

import com.miniProjet.libraryProject.DTO.EmpreuntRequestDTO;
import com.miniProjet.libraryProject.Entity.Emprunt;
import com.miniProjet.libraryProject.Service.EmpruntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.List;

// test
@Controller
@RequestMapping("api/empreunt")
@CrossOrigin("*")
public class EmpruntController {
    @Autowired
    private EmpruntService empruntService;
    @PostMapping("/add")
    public ResponseEntity<?> addEmprunt(@RequestBody EmpreuntRequestDTO empreuntRequestDTO) {
        try {
            Emprunt newEmprunt = empruntService.AddEmprunt(empreuntRequestDTO);
            return new ResponseEntity<>(newEmprunt, HttpStatus.CREATED);// "your Book is ready to take it !"
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{empruntId}")
    public ResponseEntity<?> getEmprunt(@PathVariable Long empruntId) {
        try {
            Emprunt empruntDetails = empruntService.getEmprunt(empruntId);
            return new ResponseEntity<>(empruntDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{empruntId}/retour")
    public ResponseEntity<?> setDateRetour(@PathVariable Long empruntId) {
        try {
            Emprunt empruntSetDate = empruntService.setDateRetourEffective(empruntId);
            return new ResponseEntity<>("empruntSetDate", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getHistorique/{userId}")
    public ResponseEntity<?> getEmpruntes(@PathVariable Long userId) {
        try {
            List<Emprunt> empruntsForUser = empruntService.getEmpruntsForUser(userId);
            return new ResponseEntity<>(empruntsForUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getBook/{bookId}")
    public ResponseEntity<?> getEmpruntesForBook(@PathVariable Long bookId) {
        try {
            List<Emprunt> empruntsForBook = empruntService.getEmpruntsForBook(bookId);
            return new ResponseEntity<>(empruntsForBook, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllEmprunts() {
        try {
            List<Emprunt> allEmpruntes = empruntService.getAllEmpruntsForAdmin();
            return new ResponseEntity<>(allEmpruntes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
