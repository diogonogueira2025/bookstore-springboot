package com.diogonogueira.bookstore.resources;

import com.diogonogueira.bookstore.dtos.author.AuthorRequest;
import com.diogonogueira.bookstore.dtos.author.AuthorResponse;
import com.diogonogueira.bookstore.services.AuthorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/authors")
public class AuthorResource {
    private final AuthorService authorService;

    public AuthorResource(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<Page<AuthorResponse>> findAll(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> save(@RequestBody @Valid AuthorRequest authorRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.save(authorRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> update(@PathVariable UUID id, @RequestBody @Valid AuthorRequest authorRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.update(id, authorRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        authorService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
