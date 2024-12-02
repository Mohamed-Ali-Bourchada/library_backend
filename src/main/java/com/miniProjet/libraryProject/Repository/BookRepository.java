package com.miniProjet.libraryProject.Repository;

import com.miniProjet.libraryProject.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    void deleteByIdIn(List<Long> bookIds);

    Book findByTitle(String title);
    boolean existsByAuthorAndTitle(String author, String title);

}
