package com.cursoliborges.libraryapi.service;

import com.cursoliborges.libraryapi.exception.BusinessException;
import com.cursoliborges.libraryapi.model.entity.Book;
import com.cursoliborges.libraryapi.model.repository.BookRepository;
import com.cursoliborges.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    // Simula o comportamento do BookRepository
    @MockBean
    BookRepository repository;

    //Executado antes de casa test
    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        // CENÁRIO
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString()) ).thenReturn(false);
        Mockito.when(repository.save(book)).thenReturn(
                Book.builder()
                        .id(111L)
                        .isbn("123")
                        .author("Michel")
                        .title("As aventuras")
                        .build());

        //EXECUÇÃO
        Book savedBook = service.save(book);

        //VERIFICAÇÃO
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Michel");
    }

    private Book createValidBook() {
        return Book.builder().isbn("123").author("Michel").title("As aventuras").build();
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar um livro com ISBN duplicado.")
    public void shouldNotSaveABookWithDuplicatedISBN(){
        //CENÁRIO
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString()) ).thenReturn(true);

        //EXECUÇÃO
        Throwable exception = Assertions.catchThrowable( ()-> service.save(book));

        //VERIFICAÇÃO
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("ISBN já cadastrado");
        // Mockito verifica se o respository nunca vai executar o método save com parâmetro book
        Mockito.verify(repository, Mockito.never()).save(book);

    }



}
