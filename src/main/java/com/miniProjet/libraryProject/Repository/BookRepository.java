package com.miniProjet.libraryProject.Repository;

import com.miniProjet.libraryProject.Entity.Book;
import com.miniProjet.libraryProject.Enumes.Category;
import com.miniProjet.libraryProject.Enumes.StateBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    void deleteByIdIn(List<Long> bookIds);

    //Book findByTitle(String title);
    boolean existsByAuthorAndTitle(String author, String title);

    List<Book> findByCategory(Category category);

    List<Book> findByStateBook(StateBook stateBook);

    List<Book> findByTitle(String title);
}
