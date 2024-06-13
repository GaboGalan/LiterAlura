package com.alura.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String language;

    @JsonAlias("download_count")
    private int downloadCount;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    // Ignorar la propiedad authors y mapearla manualmente
    @Transient
    @JsonProperty("authors")
    private void unpackAuthors(List<Author> authors) {
        if (authors != null && !authors.isEmpty()) {
            this.author = authors.get(0);
        }
    }

    @JsonProperty("languages")
    private void unpackLanguages(List<String> languages) {
        if (languages != null && !languages.isEmpty()) {
            this.language = languages.get(0);
        }
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", language='" + language + '\'' +
                ", downloadCount=" + downloadCount +
                ", author=" + author +
                '}';
    }
}