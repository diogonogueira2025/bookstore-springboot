package com.diogonogueira.bookstore.dtos.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record BookRequest(@NotBlank String title, @NotNull UUID publisherId, String reviewComment, @NotEmpty Set<UUID> authorIds) {
}
