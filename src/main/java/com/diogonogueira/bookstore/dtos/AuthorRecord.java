package com.diogonogueira.bookstore.dtos;

import jakarta.validation.constraints.NotBlank;

public record AuthorRecord(@NotBlank String name) {
}
