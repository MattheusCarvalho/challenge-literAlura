package br.com.alura.literalura.model;

import br.com.alura.literalura.dto.BookData;
import jakarta.persistence.*;

@Entity
  @Table(name = "books")
  public class Book {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

    @Column(unique = true)
        private String title;

    private String language;
        private Integer downloadCount;

    @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "author_id")
        private Author author;

    public Book() {}

    public Book(BookData data) {
              this.title = data.title();
              // Take only the first language
            this.language = data.languages() != null && !data.languages().isEmpty()
                              ? data.languages().get(0)
                              : "unknown";
              this.downloadCount = data.downloadCount();
    }

    // Getters & Setters
    public Long getId() { return id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public Integer getDownloadCount() { return downloadCount; }
        public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }
        public Author getAuthor() { return author; }
        public void setAuthor(Author author) { this.author = author; }

    @Override
        public String toString() {
                  String authorName = author != null ? author.getName() : "Desconhecido";
                  return """
                         +--------------------------------------+
                                     LIVRO
                                     T
