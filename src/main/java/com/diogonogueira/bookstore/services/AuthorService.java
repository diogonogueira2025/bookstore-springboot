package com.diogonogueira.bookstore.services;

import com.diogonogueira.bookstore.dtos.AuthorRecord;
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

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthorService {
    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public Page<Author> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Author findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<Author> findAllById(Set<UUID> ids) {
        List<Author> authors = repository.findAllById(ids);

        if (authors.size() != ids.size()) {
            throw new ResourceNotFoundException("One or more authors not found");
        }

        return authors;
    }

    public Author save(@NonNull AuthorRecord authorRecord) {
        Author author = new Author();
        author.setName(authorRecord.name());
        return repository.save(author);
    }

    public Author update(UUID id, @NonNull AuthorRecord authorRecord) {
        Author author = findById(id);
        author.setName(authorRecord.name());
        return repository.save(author);
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
