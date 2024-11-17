package com.miniProjet.libraryProject.Repository;

import com.miniProjet.libraryProject.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findById(Long id);

    @Override
    void deleteById(Long id);

    Book findByTitle(String title);
    boolean existsByAuthorAndTitle(String author, String title);

}
