package com.diogonogueira.bookstore.services;

import com.diogonogueira.bookstore.dtos.author.AuthorRequest;
import com.diogonogueira.bookstore.dtos.author.AuthorResponse;
import com.diogonogueira.bookstore.entities.Author;
import com.diogonogueira.bookstore.repositories.AuthorRepository;
import com.diogonogueira.bookstore.services.exceptions.DatabaseException;
import com.diogonogueira.bookstore.services.exceptions.ResourceNotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AuthorService {
    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public Page<AuthorResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::toResponse);
    }

    public Author findEntityById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public AuthorResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public List<Author> findAllById(Set<UUID> ids) {
        List<Author> authors = repository.findAllById(ids);

        if (authors.size() != ids.size()) {
            throw new ResourceNotFoundException("One or more authors not found");
        }

        return authors;
    }

    private AuthorResponse toResponse(Author author) {
        return new AuthorResponse(author.getId(), author.getName());
    }

    @Transactional
    public AuthorResponse save(@NonNull AuthorRequest authorRequest) {
        Author author = new Author();
        author.setName(authorRequest.name());
        return toResponse(repository.save(author));
    }

    @Transactional
    public AuthorResponse update(UUID id, @NonNull AuthorRequest authorRequest) {
        Author author = findEntityById(id);
        author.setName(authorRequest.name());
        return toResponse(repository.save(author));
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
