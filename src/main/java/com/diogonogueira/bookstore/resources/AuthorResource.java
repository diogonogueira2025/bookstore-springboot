package com.diogonogueira.bookstore.resources;

import com.diogonogueira.bookstore.dtos.AuthorRecord;
import com.diogonogueira.bookstore.entities.Author;
import com.diogonogueira.bookstore.services.AuthorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
public class AuthorResource {
    private final AuthorService authorService;

    public AuthorResource(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<Page<Author>> findAll(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> findById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Author> save(@RequestBody @Valid AuthorRecord authorRecord) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.save(authorRecord));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@PathVariable UUID id, @RequestBody @Valid AuthorRecord authorRecord) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.update(id, authorRecord));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        authorService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
