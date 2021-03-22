package com.cursoliborges.libraryapi.model.repository;

import com.cursoliborges.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest // Esta anotação testa o Banco de Dados em memória e depois apaga
public class BookRepositoryTest {
    @Autowired
    TestEntityManager entityManager; // Criando cénario de Banco de dados

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o ISBN informado.")
    public void returnTrueWhenIsbnExists(){
        // CENARIO
        String isbn = "123";

        Book book = Book.builder().title("As aventuras").author("Michel").isbn(isbn).build();
        entityManager.persist(book);

        // EXECUÇÃO
        boolean exists = repository.existsByIsbn(isbn);

        //VERIFICAÇÃO
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com o ISBN informado.")
    public void returnFalseWhenIsbnDoesntExists(){
        // CENARIO
        String isbn = "123";

        // EXECUÇÃO
        boolean exists = repository.existsByIsbn(isbn);

        //VERIFICAÇÃO
        assertThat(exists).isFalse();
    }
}
