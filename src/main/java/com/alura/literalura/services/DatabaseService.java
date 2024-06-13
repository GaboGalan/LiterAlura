package com.alura.literalura.services;

import com.alura.literalura.models.Author;
import com.alura.literalura.models.Book;
import com.alura.literalura.repositories.AuthorRepository;
import com.alura.literalura.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DatabaseService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public DatabaseService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public List<Book> listarLibrosRegistrados() {
        return bookRepository.findAll();
    }

    public List<Author> listarAutoresRegistrados() {
        return authorRepository.findAll();
    }

    public List<String> listarLibrosPorAutor(Long authorId) {
        List<Book> books = bookRepository.findByAuthorId(authorId);
        return books.stream().map(Book::getTitle).collect(Collectors.toList());
    }

    public List<Book> listarLibrosPorIdioma(String idioma) {
        return bookRepository.findByLanguage(idioma);
    }

    public List<Author> listarAutoresVivos(int year) {
        return authorRepository.findAuthorsAliveInYear(year);
    }

    public Optional<Book> findBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public void saveBook(Book book) {
        // Verificar si el autor ya existe en la base de datos
        Optional<Author> existingAuthor = authorRepository.findByNameAndBirthYearAndDeathYear(
                book.getAuthor().getName(),
                book.getAuthor().getBirthYear(),
                book.getAuthor().getDeathYear()
        );

        // Si el autor existe, usar el autor existente
        if (existingAuthor.isPresent()) {
            book.setAuthor(existingAuthor.get());
        } else {
            // Si no existe, guardar el nuevo autor
            Author author = book.getAuthor();
            author = authorRepository.save(author);
            book.setAuthor(author);
        }

        // Guardar el libro
        bookRepository.save(book);
    }
}