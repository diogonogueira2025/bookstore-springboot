package com.diogonogueira.bookstore.dtos;

import jakarta.validation.constraints.NotBlank;

public record PublisherRecord(@NotBlank String name) {
}
