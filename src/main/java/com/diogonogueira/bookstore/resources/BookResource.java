package com.diogonogueira.bookstore.resources;

import com.diogonogueira.bookstore.dtos.BookRecord;
import com.diogonogueira.bookstore.entities.Book;
import com.diogonogueira.bookstore.services.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookResource {
    private final BookService bookService;

    public BookResource(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Book> save(@RequestBody @Valid BookRecord bookRecord) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(bookRecord));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable UUID id, @RequestBody @Valid BookRecord bookRecord) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.update(id, bookRecord));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        bookService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}