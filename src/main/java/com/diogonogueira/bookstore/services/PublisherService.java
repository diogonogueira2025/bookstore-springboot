package com.diogonogueira.bookstore.services;

import com.diogonogueira.bookstore.dtos.PublisherRecord;
import com.diogonogueira.bookstore.entities.Publisher;
import com.diogonogueira.bookstore.repositories.PublisherRepository;
import com.diogonogueira.bookstore.services.exceptions.DatabaseException;
import com.diogonogueira.bookstore.services.exceptions.ResourceNotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PublisherService {
    private final PublisherRepository repository;

    public PublisherService(PublisherRepository repository) {
        this.repository = repository;
    }

    public List<Publisher> findAll() {
        return repository.findAll();
    }

    public Publisher findById(UUID id) {
        return repository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
    }

    public Publisher save(@NonNull PublisherRecord publisherRecord) {
        Publisher publisher = new Publisher();
        publisher.setName(publisherRecord.name());
        return repository.save(publisher);
    }

    public Publisher update(UUID id, @NonNull PublisherRecord publisherRecord) {
        Publisher publisher = findById(id);
        publisher.setName(publisherRecord.name());
        return repository.save(publisher);
    }

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
