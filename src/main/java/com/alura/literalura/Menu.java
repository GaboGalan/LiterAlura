package com.alura.literalura;

import com.alura.literalura.models.Author;
import com.alura.literalura.models.Book;
import com.alura.literalura.services.GutendexService;
import com.alura.literalura.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
public class Menu {
    private final GutendexService gutendexService;
    private final DatabaseService databaseService;
    private final Scanner scanner;

    @Autowired
    public Menu(GutendexService gutendexService, DatabaseService databaseService) {
        this.gutendexService = gutendexService;
        this.databaseService = databaseService;
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        int opcion;

        while (true) {
            System.out.println("===== Catálogo de Libros =====");
            System.out.println("1. Buscar libro por título");
            System.out.println("2. Listar libros registrados");
            System.out.println("3. Listar autores registrados");
            System.out.println("4. Listar autores vivos en un determinado año");
            System.out.println("5. Listar libros por idioma");
            System.out.println("0. Salir");
            System.out.print("Elija una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();  // Consumir el salto de línea

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivos();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saliendo del programa...");
                        return;
                    default:
                        System.out.println("Opción no válida. Por favor, elija una opción del 0 al 5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                scanner.nextLine(); // Limpiar el buffer del scanner
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();
        try {
            String resultado = gutendexService.buscarLibroPorTitulo(titulo);
            System.out.println(resultado);
        } catch (Exception e) {
            System.out.println("Error al buscar el libro: " + e.getMessage());
        }
    }

    private void listarLibrosRegistrados() {
        try {
            List<Book> books = databaseService.listarLibrosRegistrados();
            if (books.isEmpty()) {
                System.out.println("No hay libros registrados.");
            } else {
                for (Book book : books) {
                    System.out.println("----- LIBRO -----");
                    System.out.println("Título: " + book.getTitle());
                    System.out.println("Autor: " + book.getAuthor().getName());
                    System.out.println("Idioma: " + book.getLanguage());
                    System.out.println("Número de descargas: " + book.getDownloadCount());
                    System.out.println("-----------------");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar libros registrados: " + e.getMessage());
        }
    }

    private void listarAutoresRegistrados() {
        try {
            List<Author> authors = databaseService.listarAutoresRegistrados();
            if (authors.isEmpty()) {
                System.out.println("No hay autores registrados.");
            } else {
                for (Author author : authors) {
                    System.out.println("----- AUTOR -----");
                    System.out.println("Nombre: " + author.getName());
                    System.out.println("Fecha de nacimiento: " + (author.getBirthYear() != null ? author.getBirthYear() : "Desconocida"));
                    System.out.println("Fecha de fallecimiento: " + (author.getDeathYear() != null ? author.getDeathYear() : "Desconocida"));

                    List<String> bookTitles = databaseService.listarLibrosPorAutor(author.getId());
                    System.out.println("Libros: " + bookTitles);
                    System.out.println("-----------------");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar autores registrados: " + e.getMessage());
        }
    }

    private void listarAutoresVivos() {
        System.out.print("Ingrese el año para listar autores vivos: ");
        try {
            int año = scanner.nextInt();
            scanner.nextLine();  // Consumir el salto de línea

            List<Author> authors = databaseService.listarAutoresVivos(año);
            if (authors.isEmpty()) {
                System.out.println("No hay autores vivos en el año: " + año);
            } else {
                for (Author author : authors) {
                    System.out.println("----- AUTOR -----");
                    System.out.println("Nombre: " + author.getName());
                    System.out.println("Fecha de nacimiento: " + (author.getBirthYear() != null ? author.getBirthYear() : "Desconocida"));
                    System.out.println("Fecha de fallecimiento: " + (author.getDeathYear() != null ? author.getDeathYear() : "Desconocida"));

                    List<String> bookTitles = databaseService.listarLibrosPorAutor(author.getId());
                    System.out.println("Libros: " + bookTitles);
                    System.out.println("-----------------");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada no válida. Por favor, ingrese un número.");
            scanner.nextLine(); // Limpiar el buffer del scanner
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Seleccione un idioma:");
        System.out.println("en - English");
        System.out.println("es - Español");
        System.out.println("fr - Française");
        System.out.println("pt - Português");
        System.out.print("Elija una opción: ");

        String idioma = scanner.nextLine().trim().toLowerCase();

        try {
            List<Book> books = databaseService.listarLibrosPorIdioma(idioma);
            if (books.isEmpty()) {
                System.out.println("No hay libros en el idioma: " + idioma);
            } else {
                for (Book book : books) {
                    System.out.println("----- LIBRO -----");
                    System.out.println("Título: " + book.getTitle());
                    System.out.println("Autor: " + book.getAuthor().getName());
                    System.out.println("Idioma: " + book.getLanguage());
                    System.out.println("Número de descargas: " + book.getDownloadCount());
                    System.out.println("-----------------");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar libros por idioma: " + e.getMessage());
        }
    }
}