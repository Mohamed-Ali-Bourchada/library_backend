package com.miniProjet.libraryProject.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniProjet.libraryProject.DTO.BookRequestDTO;
import com.miniProjet.libraryProject.Entity.Book;
import com.miniProjet.libraryProject.Entity.Users;
import com.miniProjet.libraryProject.Enumes.StateBook;
import com.miniProjet.libraryProject.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public void CreateBook(BookRequestDTO bookDto, MultipartFile cover) throws IOException, DataFormatException {

        if (bookRepository.existsByAuthorAndTitle(bookDto.getAuthor(), bookDto.getTitle())) {
            throw new RuntimeException("this book is already registered.");
        } else {
            Book book = new Book();
            book.setAuthor(bookDto.getAuthor());
            book.setTitle(bookDto.getTitle());
            book.setCategory(bookDto.getCategory());
            book.setSujet(bookDto.getSujet());
            book.setStateBook(StateBook.disponible);
            book.setCover(compressBytes(cover.getBytes()));
            bookRepository.save(book);
        }
    }

    public List<Book> GetAllBooks() {
        List<Book> books = bookRepository.findAll();
        for (Book book : books) {
            if (book.getCover() != null) {
                book.setCover(compressBytes(book.getCover()));
            }
        }
        return books;
    }

    public Book UpdateBook(Long id, BookRequestDTO bookDto) throws IOException, DataFormatException {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("this book is not registered"));
        existingBook.setTitle(bookDto.getTitle());
        existingBook.setAuthor(bookDto.getAuthor());
        existingBook.setCategory(bookDto.getCategory());
        existingBook.setSujet(bookDto.getSujet());
        // existingBook.setCover(compressBytes(cover.getBytes()));
        bookRepository.save(existingBook);
        return existingBook;
    }

    public void UpdateCoverBook(Long id, MultipartFile cover) throws IOException, DataFormatException {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("book not found to update cover"));
        existingBook.setCover(compressBytes(cover.getBytes()));
        bookRepository.save(existingBook);

    }

    public void deleteBook(Long id) throws IOException, DataFormatException {
        bookRepository.deleteById(id);
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }

}
