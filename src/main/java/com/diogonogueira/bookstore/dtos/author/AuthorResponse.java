package com.diogonogueira.bookstore.dtos.author;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record AuthorResponse(UUID id, String name) {
}
