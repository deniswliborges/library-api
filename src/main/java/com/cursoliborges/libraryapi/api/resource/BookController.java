package com.cursoliborges.libraryapi.api.resource;

import com.cursoliborges.libraryapi.api.dto.BookDTO;
//import com.cursoliborges.libraryapi.api.exception.ApiErrors;
import com.cursoliborges.libraryapi.api.exception.ApiErros;
import com.cursoliborges.libraryapi.exception.BusinessException;
import com.cursoliborges.libraryapi.model.entity.Book;
import com.cursoliborges.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService service;
    private ModelMapper modelMapper;

    public BookController(BookService service,ModelMapper mapper) {
        this.service = service;
        this.modelMapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto){

        Book entity = modelMapper.map(dto,Book.class);

        entity = service.save(entity);

        return modelMapper.map(entity,BookDTO.class);
    }

    //Toda vez que for tratado um exceptionHandler, ou seja erro de resposta, será chamada esta class MethodArgumentNotValidException.class que esta dentro do metodo public ApiErrors......
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //Esta anotação retorna o erro BadRequest
    public ApiErros handleValidationExceptions(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErros(bindingResult);
    }
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleBusinessException(BusinessException ex){

        return new ApiErros(ex);
    }

}
