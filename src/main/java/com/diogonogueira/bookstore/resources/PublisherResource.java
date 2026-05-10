package com.diogonogueira.bookstore.resources;

import com.diogonogueira.bookstore.dtos.publisher.PublisherRequest;
import com.diogonogueira.bookstore.dtos.publisher.PublisherResponse;
import com.diogonogueira.bookstore.services.PublisherService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/publishers")
public class PublisherResource {
    private final PublisherService publisherService;

    public PublisherResource(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<Page<PublisherResponse>> findAll(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(publisherService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(publisherService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PublisherResponse> save(@RequestBody @Valid PublisherRequest publisherRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publisherService.save(publisherRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponse> update(@PathVariable UUID id, @RequestBody @Valid PublisherRequest publisherRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(publisherService.update(id, publisherRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        publisherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
