package com.alura.literalura.repositories;

import com.alura.literalura.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthorId(Long authorId);
    List<Book> findByLanguage(String language);
    Optional<Book> findByTitle(String title);
}

