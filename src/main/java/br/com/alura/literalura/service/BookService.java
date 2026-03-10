package br.com.alura.literalura.service;

import br.com.alura.literalura.dto.BookData;
import br.com.alura.literalura.dto.SearchResult;
import br.com.alura.literalura.model.Author;
import br.com.alura.literalura.model.Book;
import br.com.alura.literalura.repository.AuthorRepository;
import br.com.alura.literalura.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class BookService {

    private static final String GUTENDEX_BASE_URL = "https://gutendex.com/books/?search=";

    private final ApiConsumer apiConsumer = new ApiConsumer();
    private final DataConverter converter = new DataConverter();

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // ──────────────────────────────────────────────────
    //  OPTION 1 – Buscar livro pelo título (API + DB)
    // ──────────────────────────────────────────────────
    public void searchAndSaveBook(Scanner scanner) {
        System.out.print("\nDigite o título do livro que deseja buscar: ");
        String title = scanner.nextLine().trim();

        if (title.isBlank()) {
            System.out.println("⚠  Título inválido.");
            return;
        }

        // Check if it already exists in the DB
        Optional<Book> existing = bookRepository.findByTitleContainingIgnoreCase(title);
        if (existing.isPresent()) {
            System.out.println("\n⚠  Livro já cadastrado no banco de dados:");
            System.out.println(existing.get());
            return;
        }

        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String json = apiConsumer.getData(GUTENDEX_BASE_URL + encodedTitle);
        SearchResult result = converter.convert(json, SearchResult.class);

        if (result.results() == null || result.results().isEmpty()) {
            System.out.println("\n⚠  Nenhum livro encontrado para: \"" + title + "\"");
            return;
        }

        BookData bookData = result.results().get(0); // take first result

        if (bookData.authors() == null || bookData.authors().isEmpty()) {
            System.out.println("\n⚠  O livro encontrado não possui dados de autor.");
            return;
        }

        // Resolve or create author
        String authorName = bookData.authors().get(0).name();
        Author author = authorRepository.findByName(authorName)
                .orElseGet(() -> {
                    Author newAuthor = new Author(bookData.authors().get(0));
                    return authorRepository.save(newAuthor);
                });

        // Create and link book
        Book book = new Book(bookData);
        book.setAuthor(author);
        bookRepository.save(book);

        System.out.println("\n✅ Livro salvo com sucesso!");
        System.out.println(book);
    }

    // ──────────────────────────────────────────────────
    //  OPTION 2 – Listar livros registrados
    // ──────────────────────────────────────────────────
    public void listSavedBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("\n⚠  Nenhum livro cadastrado ainda. Use a opção 1 para buscar livros.");
            return;
        }
        System.out.println("\n📚 Livros registrados no banco de dados:\n");
        books.forEach(System.out::println);
    }

    // ──────────────────────────────────────────────────
    //  OPTION 3 – Listar autores registrados
    // ──────────────────────────────────────────────────
    public void listAuthors() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            System.out.println("\n⚠  Nenhum autor cadastrado ainda.");
            return;
        }
        System.out.println("\n✍  Autores registrados no banco de dados:\n");
        authors.forEach(System.out::println);
    }

    // ──────────────────────────────────────────────────
    //  OPTION 4 – Listar autores vivos em determinado ano
    // ──────────────────────────────────────────────────
    public void listAuthorsAliveInYear(Scanner scanner) {
        System.out.print("\nDigite o ano que deseja pesquisar: ");
        String input = scanner.nextLine().trim();
        int year;
        try {
            year = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("⚠  Ano inválido. Digite apenas números.");
            return;
        }

        // Authors with birth <= year AND (death >= year OR death is null)
        List<Author> alive = new ArrayList<>(
                authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year)
        );
        alive.addAll(
                authorRepository.findByBirthYearLessThanEqualAndDeathYearIsNull(year)
        );

        if (alive.isEmpty()) {
            System.out.println("\n⚠  Nenhum autor estava vivo no ano " + year + ".");
            return;
        }

        System.out.println("\n✍  Autores vivos no ano " + year + ":\n");
        alive.forEach(System.out::println);
    }

    // ──────────────────────────────────────────────────
    //  OPTION 5 – Listar livros em determinado idioma
    // ──────────────────────────────────────────────────
    public void listBooksByLanguage(Scanner scanner) {
        System.out.println("""

               Idiomas disponíveis:
                 en  – Inglês
                 pt  – Português
                 es  – Espanhol
                 fr  – Francês
               """);
        System.out.print("Digite o idioma desejado: ");
        String lang = scanner.nextLine().trim().toLowerCase();

        List<String> valid = List.of("en", "pt", "es", "fr");
        if (!valid.contains(lang)) {
            System.out.println("⚠  Idioma não suportado. Escolha entre: en, pt, es, fr.");
            return;
        }

        List<Book> books = bookRepository.findByLanguageIgnoreCase(lang);
        if (books.isEmpty()) {
            System.out.println("\n⚠  Não existem livros no idioma \"" + lang.toUpperCase() + "\" no banco de dados.");
            return;
        }

        System.out.println("\n📚 Livros no idioma \"" + lang.toUpperCase() + "\":\n");
        books.forEach(System.out::println);
    }
}
