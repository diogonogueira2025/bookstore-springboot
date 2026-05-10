package com.diogonogueira.bookstore.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;
import java.util.UUID;

public record BookRecord(@NotBlank String title, UUID publisherId, String reviewComment, Set<UUID> authorsId) {
}
