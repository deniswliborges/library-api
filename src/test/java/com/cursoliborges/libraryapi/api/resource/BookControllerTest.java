package com.cursoliborges.libraryapi.api.resource;

import com.cursoliborges.libraryapi.api.dto.BookDTO;
import com.cursoliborges.libraryapi.exception.BusinessException;
import com.cursoliborges.libraryapi.model.entity.Book;
import com.cursoliborges.libraryapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.regex.Matcher;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// @RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class) // Anotação para executar class de Test no JUnit 5
@ActiveProfiles("test") // Executar com Perfil de Test
@WebMvcTest // Testando os comportamentos das APIs
@AutoConfigureMockMvc // Spring configura Objeto para fazer as requisições
public class BookControllerTest {

    static String BOOK_API = "/api/books"; // Criando rota para API

    @Autowired
    MockMvc mvc;
    @MockBean // Anotação que cria a class instanciada("fake") dentro do contexto @ExtendWith
    BookService service;

    @Test
    @DisplayName("Deve criar um Livro com sucesso.")
    public void createBookTest() throws Exception{

        BookDTO dto = createNewBook();
        Book savebook = Book.builder().id(111L).author("Denis Willian").title("Mockito").isbn("123456").build();

        // SIMULANDO COMPORTAMENTO DO MÉTODO "SAVE" INJETADO DO @MockBean DA CLASS BookService
        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savebook);

        // Transforma objeto em Jason
        String json = new ObjectMapper().writeValueAsString(dto); // Instanciando para adicionar ao .content("");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders // Defini uma requisição CRUD
                .post(BOOK_API) // Executa CRUD a partir da rota criada
                .contentType(MediaType.APPLICATION_JSON) // Passando conteúdo do Tipo Media JSON
                .accept(MediaType.APPLICATION_JSON) // Servidor aceita requisições do Tipo JSON
                .content(json);// Passar o corpo da Requisição

        // Fazendo a requisição
        mvc      // Estruturando como tem que aparecer na tela no formato Json
                .perform(request) // Recebe a requisição "request" definida acima do MockHttpServletRequestBuilder
                .andExpect(status().isCreated()) // Verifica a requisição POst dos Metchers MVC que foi criado
                .andExpect(jsonPath("id").isNotEmpty() ) // Espero que, o retorno com ID já populado, ou seja, com todas as informações
                .andExpect(jsonPath("title").value(dto.getTitle()) ) // Espero que, retorne com Titulo "Meu Livro"
                .andExpect(jsonPath("author").value(dto.getAuthor()) ) // Espero que, retorne com Titulo "Autor"
                .andExpect(jsonPath("isbn").value(dto.getIsbn()) ) // Espero que, retorne com Titulo "ISBN"
        ;
    }



    @Test
    @DisplayName("Deve lançar erro de validação quando não houver" +
            " dados suficiente para criação do livro.")
    public void createInvalidBookTest() throws Exception{
        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders // Defini uma requisição CRUD
                .post(BOOK_API) // Executa CRUD a partir da rota criada
                .contentType(MediaType.APPLICATION_JSON) // Passando conteúdo do Tipo Media JSON
                .accept(MediaType.APPLICATION_JSON) // Servidor aceita requisições do Tipo JSON
                .content(json);
        // Fazendo a requisição
        mvc.perform(request)
                .andExpect(status().isBadRequest() )    // Informando que três atributos BookDTO sejam informados
                .andExpect(jsonPath("erros", hasSize(3)));
    }

    @Test
    @DisplayName("Deve Lançar erro ao tentar cadastrar um livro com ISBN já utilizado por outro.")
    public void createBookWithDuplicatedIsbn() throws Exception{

        BookDTO dto = createNewBook();

        String json = new ObjectMapper().writeValueAsString(dto);
        String mensagemErro = "ISBN já cadastrado";
        BDDMockito.given(service.save(Mockito.any(Book.class)))
                .willThrow(new BusinessException(mensagemErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders // Defini uma requisição CRUD
                .post(BOOK_API) // Executa CRUD a partir da rota criada
                .contentType(MediaType.APPLICATION_JSON) // Passando conteúdo do Tipo Media JSON
                .accept(MediaType.APPLICATION_JSON) // Servidor aceita requisições do Tipo JSON
                .content(json);
        // Fazendo a requisição
        mvc.perform(request)
                .andExpect(status().isBadRequest() )    // Informando que três atributos BookDTO sejam informados
                .andExpect(jsonPath("erros", hasSize(1)))
                .andExpect(jsonPath("erros[0]").value(mensagemErro));
    }

    private BookDTO createNewBook() {
        return BookDTO.builder().author("Denis Willian").title("Mockito").isbn("123456").build();
    }
}
