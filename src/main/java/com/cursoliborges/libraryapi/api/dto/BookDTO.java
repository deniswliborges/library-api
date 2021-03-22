package com.cursoliborges.libraryapi.api.dto;


import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@Builder // Cria o construtor com todos parâmetros. Após adicionar anotações @NoArgsConstructor e @AllArgsConstructorprecisa abaixo dele
@NoArgsConstructor // Criando construtor sem argumentos
@AllArgsConstructor // Criando construtor com argumentos
public class BookDTO {
    private Long id;

    @NotEmpty
    private String title;
    @NotEmpty
    private String author;
    @NotEmpty
    private String isbn;

}
