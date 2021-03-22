package com.cursoliborges.libraryapi.service.impl;

import com.cursoliborges.libraryapi.exception.BusinessException;
import com.cursoliborges.libraryapi.model.entity.Book;
import com.cursoliborges.libraryapi.model.repository.BookRepository;
import com.cursoliborges.libraryapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    //@Autowired
    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())){
            throw new BusinessException("ISBN já cadastrado");
        }

        return repository.save(book);
    }
}
