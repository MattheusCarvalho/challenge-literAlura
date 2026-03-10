package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByName(String name);

    // Authors whose birth year <= year AND death year >= year (alive in that year)
    // Also include authors with null death_year (still alive / unknown)
    List<Author> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(Integer year, Integer year2);

    List<Author> findByBirthYearLessThanEqualAndDeathYearIsNull(Integer year);
}
