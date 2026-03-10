package br.com.alura.literalura.model;

import br.com.alura.literalura.dto.AuthorData;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
  @Table(name = "authors")
  public class Author {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

    @Column(unique = true)
        private String name;

    private Integer birthYear;
        private Integer deathYear;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private List<Book> books = new ArrayList<>();

    public Author() {}

    public Author(AuthorData data) {
              this.name = data.name();
              this.birthYear = data.birthYear();
              this.deathYear = data.deathYear();
    }

    // Getters & Setters
    public Long getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getBirthYear() { return birthYear; }
        public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }
        public Integer getDeathYear() { return deathYear; }
        public void setDeathYear(Integer deathYear) { this.deathYear = deathYear; }
        public List<Book> getBooks() { return books; }
        public void setBooks(List<Book> books) { this.books = books; }

    @Override
        public String toString() {
                  String birth = birthYear != null ? String.valueOf(birthYear) : "Desconhecido";
                  String death = deathYear != null ? String.valueOf(deathYear) : "Desconhecido";
                  List<String> bookTitles = books.stream().map(Book::getTitle).toList();
                  return """
                         +--------------------------------------+
                                     AUTOR
                                     Nome:        %s
                                     Nascimento:  %s
                                     Falecimento: %s
                                     Livros:      %s
                                   +--------------------------------------+
                                   """.formatted(name, birth, death, bookTitles);
        }
  }
