package com.alura.literalura.services;

import com.alura.literalura.models.Book;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GutendexService {
    private static final String BASE_URL = "https://gutendex.com/books/";

    private final DatabaseService databaseService;

    public GutendexService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public String buscarLibroPorTitulo(String titulo) {
        String url = BASE_URL + "?search=" + titulo.replace(" ", "+");

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response);
            JsonNode resultsNode = jsonNode.get("results");

            if (resultsNode != null && resultsNode.isArray() && resultsNode.size() > 0) {
                JsonNode firstResult = resultsNode.get(0);
                Book book = mapper.readValue(firstResult.toString(), Book.class);

                // Verificar si el libro ya existe en la base de datos
                Optional<Book> existingBook = databaseService.findBookByTitle(book.getTitle());
                if (existingBook.isPresent()) {
                    return "Libro ya en BD, no es posible agregarlo";
                }

                databaseService.saveBook(book);
                return formatearMensajeLibro(book);
            } else {
                return "No se encontró ningún libro con ese título.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al buscar el libro: " + e.getMessage();
        }
    }

    private String formatearMensajeLibro(Book book) {
        return "----- LIBRO -----\n" +
                "Título: " + book.getTitle() + "\n" +
                "Autor: " + book.getAuthor().getName() + "\n" +
                "Idioma: " + book.getLanguage() + "\n" +
                "Número de descargas: " + book.getDownloadCount() + "\n" +
                "-----------------";
    }
}