package com.miniProjet.libraryProject.Service;

import com.miniProjet.libraryProject.DTO.EmpreuntRequestDTO;
import com.miniProjet.libraryProject.Entity.Book;
import com.miniProjet.libraryProject.Entity.Emprunt;
import com.miniProjet.libraryProject.Entity.Users;
import com.miniProjet.libraryProject.Enumes.StateBook;
import com.miniProjet.libraryProject.Repository.BookRepository;
import com.miniProjet.libraryProject.Repository.EmpreuntRepository;
import com.miniProjet.libraryProject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmpruntService {
    @Autowired
    private EmpreuntRepository empreuntRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    public Emprunt AddEmprunt(EmpreuntRequestDTO empreuntRequestDTO) {
        // Vérifier si book et user sont présents dans le DTO
        if (empreuntRequestDTO.getBook() == null || empreuntRequestDTO.getBook().getId() == null) {
            throw new RuntimeException("Book information is missing or invalid.");
        }
        if (empreuntRequestDTO.getUser() == null || empreuntRequestDTO.getUser().getId() == null) {
            throw new RuntimeException("User information is missing or invalid.");
        }
        if (empreuntRepository.existsByBook_IdAndUser_Id(empreuntRequestDTO.getBook().getId(),
                empreuntRequestDTO.getUser().getId())) {
            throw new RuntimeException("this emprenut is ready exist !");
        } else {

            // Récupérer le livre par son ID
            Book book = bookRepository.findById(empreuntRequestDTO.getBook().getId())
                    .orElseThrow(() -> new RuntimeException("Book not found. Please refresh the data."));

            // Récupérer l'utilisateur par son ID
            Users user = userRepository.findById(empreuntRequestDTO.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User account not found. Please login."));

            if (book.getStateBook() == StateBook.indisponible) {
                throw new RuntimeException("This book is indisponible !");

            }

            // Vérification de la date de retour prévue
            if (empreuntRequestDTO.getDateRoutourPrevu() == null) {
                throw new RuntimeException("Please set a date to return the book.");
            }

            // Créer l'emprunt
            Emprunt emprunt = new Emprunt();

            emprunt.setBook(book);
            emprunt.setUser(user);
            emprunt.setDateEmprunt(LocalDate.now());
            emprunt.setDateRoutourPrevu(empreuntRequestDTO.getDateRoutourPrevu());

            book.setStateBook(StateBook.indisponible);
            bookRepository.save(book);

            // Sauvegarder l'emprunt
            return empreuntRepository.save(emprunt);
        }
    }

    public List<Emprunt> getAllEmpruntsForAdmin() {
        return empreuntRepository.findAll();
    }

    public List<Emprunt> getEmpruntsForUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return empreuntRepository.findByUser_Id(userId);
    }

    public List<Emprunt> getEmpruntsForBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return empreuntRepository.findByBook_Id(bookId);
    }

    public Emprunt getEmprunt(Long id) {
        return empreuntRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt not found. Please refresh the data."));

    }

    public Emprunt setDateRetourEffective(long emprentId) {
        Emprunt empruntExiste = empreuntRepository.findById(emprentId)
                .orElseThrow(() -> new RuntimeException("Book not found. Please refresh the data."));
        empruntExiste.setDateRoutourEffective(LocalDate.now());
        Book book =empruntExiste.getBook();
        book.setStateBook(StateBook.disponible);
        bookRepository.save(book);
        return empreuntRepository.save(empruntExiste);
    }
    // getAllEmpruntes pour admin
    // getAllEmpruntes pour chaque user
    // setDateRetour
}
