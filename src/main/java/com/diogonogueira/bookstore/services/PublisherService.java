package com.diogonogueira.bookstore.services;

import com.diogonogueira.bookstore.dtos.publisher.PublisherRequest;
import com.diogonogueira.bookstore.dtos.publisher.PublisherResponse;
import com.diogonogueira.bookstore.entities.Publisher;
import com.diogonogueira.bookstore.repositories.PublisherRepository;
import com.diogonogueira.bookstore.services.exceptions.DatabaseException;
import com.diogonogueira.bookstore.services.exceptions.ResourceNotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PublisherService {
    private final PublisherRepository repository;

    public PublisherService(PublisherRepository repository) {
        this.repository = repository;
    }

    public Publisher findEntityById(UUID id) {
        return repository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
    }

    private PublisherResponse toResponse(Publisher publisher) {
        return new PublisherResponse(publisher.getId(), publisher.getName());
    }

    public Page<PublisherResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public PublisherResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    @Transactional
    public PublisherResponse save(@NonNull PublisherRequest publisherRequest) {
        Publisher publisher = new Publisher();
        publisher.setName(publisherRequest.name());
        return toResponse(repository.save(publisher));
    }

    @Transactional
    public PublisherResponse update(UUID id, @NonNull PublisherRequest publisherRequest) {
        Publisher publisher = findEntityById(id);
        publisher.setName(publisherRequest.name());
        return toResponse(repository.save(publisher));
    }

    @Transactional
    public void deleteById(UUID id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }

    }
}
