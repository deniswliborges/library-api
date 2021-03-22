package com.cursoliborges.libraryapi.model.repository;

import com.cursoliborges.libraryapi.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface BookRepository extends JpaRepository <Book, Long>{
    boolean existsByIsbn(String isbn);
}
