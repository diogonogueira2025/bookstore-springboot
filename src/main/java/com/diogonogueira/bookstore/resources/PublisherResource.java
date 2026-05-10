package com.diogonogueira.bookstore.resources;

import com.diogonogueira.bookstore.dtos.PublisherRecord;
import com.diogonogueira.bookstore.entities.Publisher;
import com.diogonogueira.bookstore.services.PublisherService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/publishers")
public class PublisherResource {
    private final PublisherService publisherService;

    public PublisherResource(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<Page<Publisher>> findAll(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(publisherService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publisher> findById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(publisherService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Publisher> save(@RequestBody @Valid PublisherRecord publisherRecord) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publisherService.save(publisherRecord));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> update(@PathVariable UUID id, @RequestBody @Valid PublisherRecord publisherRecord) {
        return ResponseEntity.status(HttpStatus.OK).body(publisherService.update(id, publisherRecord));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        publisherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
