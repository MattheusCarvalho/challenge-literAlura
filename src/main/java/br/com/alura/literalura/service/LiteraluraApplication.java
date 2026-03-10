package br.com.alura.literalura;

import br.com.alura.literalura.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.InputMismatchException;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    private final BookService bookService;

    public LiteraluraApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        displayMenu();
    }

    private void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        while (option != 0) {
            System.out.println("""

                    ╔════════════════════════════════════╗
                    ║       📖  L I T E R A L U R A      ║
                    ╠════════════════════════════════════╣
                    ║  1 - Buscar livro pelo título       ║
                    ║  2 - Listar livros registrados      ║
                    ║  3 - Listar autores registrados     ║
                    ║  4 - Listar autores vivos em ano    ║
                    ║  5 - Listar livros por idioma       ║
                    ║  0 - Sair                           ║
                    ╚════════════════════════════════════╝
                    """);
            System.out.print("Escolha uma opção: ");

            try {
                option = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠  Opção inválida. Digite um número entre 0 e 5.\n");
                continue;
            }

            switch (option) {
                case 1 -> bookService.searchAndSaveBook(scanner);
                case 2 -> bookService.listSavedBooks();
                case 3 -> bookService.listAuthors();
                case 4 -> bookService.listAuthorsAliveInYear(scanner);
                case 5 -> bookService.listBooksByLanguage(scanner);
                case 0 -> System.out.println("\n👋 Encerrando o LiterAlura. Até logo!");
                default -> System.out.println("⚠  Opção inválida. Escolha entre 0 e 5.\n");
            }
        }

        scanner.close();
    }
}
