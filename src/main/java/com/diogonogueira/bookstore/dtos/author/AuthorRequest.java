package com.diogonogueira.bookstore.dtos.author;

import jakarta.validation.constraints.NotBlank;

public record AuthorRequest(@NotBlank String name) {
}
