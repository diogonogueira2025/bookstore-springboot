package com.diogonogueira.bookstore.services;

import com.diogonogueira.bookstore.dtos.book.BookRequest;
import com.diogonogueira.bookstore.dtos.book.BookResponse;
import com.diogonogueira.bookstore.entities.Author;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository repository;
    private final PublisherService publisherService;
    private final AuthorService authorService;

    public BookService(BookRepository repository, PublisherService publisherService, AuthorService authorService) {
        this.repository = repository;
        this.publisherService = publisherService;
        this.authorService = authorService;
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getPublisher().getId(),
                book.getReview() != null ? book.getReview().getComment() : null,
                book.getAuthors().stream().map(Author::getId).collect(Collectors.toSet()));
    }

    public Book findEntityById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Page<BookResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public BookResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    @Transactional
    public BookResponse save(@NonNull BookRequest bookRequest) {
        Book book = new Book();
        mapRecordToBook(book, bookRequest);
        return toResponse(repository.save(book));
    }

    @Transactional
    public BookResponse update(UUID id, @NonNull BookRequest bookRequest) {
        Book book = findEntityById(id);
        mapRecordToBook(book, bookRequest);
        return toResponse(repository.save(book));
    }

    private void mapRecordToBook(Book book, BookRequest bookRequest) {
        book.setTitle(bookRequest.title());
        book.getAuthors().clear();
        book.getAuthors().addAll(authorService.findAllById(bookRequest.authorIds()));
        book.setPublisher(publisherService.findEntityById(bookRequest.publisherId()));

        String reviewComment = bookRequest.reviewComment();

        if (reviewComment != null && !reviewComment.isBlank()) {
            Review review = book.getReview();

            if (review == null) {
                review = new Review();
                review.setBook(book);
                book.setReview(review);
            }
            review.setComment(reviewComment);
        }
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
