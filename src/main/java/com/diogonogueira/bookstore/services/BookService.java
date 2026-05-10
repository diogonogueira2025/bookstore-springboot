package com.diogonogueira.bookstore.services;

import com.diogonogueira.bookstore.dtos.BookRecord;
import com.diogonogueira.bookstore.entities.Book;
import com.diogonogueira.bookstore.entities.Review;
import com.diogonogueira.bookstore.repositories.BookRepository;
import com.diogonogueira.bookstore.services.exceptions.DatabaseException;
import com.diogonogueira.bookstore.services.exceptions.ResourceNotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository repository;
    private final PublisherService publisherService;
    private final AuthorService authorService;

    public BookService(BookRepository repository, PublisherService publisherService, AuthorService authorService) {
        this.repository = repository;
        this.publisherService = publisherService;
        this.authorService = authorService;
    }

    public Page<Book> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Book findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Book save(@NonNull BookRecord bookRecord) {
        Book book = new Book();
        mapRecordToBook(book, bookRecord);

        return repository.save(book);
    }

    public Book update(UUID id, @NonNull BookRecord bookRecord) {
        Book book = findById(id);

        mapRecordToBook(book, bookRecord);

        return repository.save(book);
    }

    private void mapRecordToBook(Book book, BookRecord bookRecord) {
        book.setTitle(bookRecord.title());
        book.getAuthors().clear();
        book.getAuthors().addAll(authorService.findAllById(bookRecord.authorsId()));
        book.setPublisher(publisherService.findById(bookRecord.publisherId()));

        String reviewComment = bookRecord.reviewComment();

        if (reviewComment != null && !reviewComment.isBlank()) {
            Review review = new Review();

            review.setBook(book);
            review.setComment(bookRecord.reviewComment());

            book.setReview(review);
        }
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
