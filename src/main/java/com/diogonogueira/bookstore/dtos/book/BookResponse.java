package com.diogonogueira.bookstore.dtos.book;

import java.util.Set;
import java.util.UUID;

public record BookResponse(UUID id, String title, UUID publisherId, String reviewComment, Set<UUID> authorIds) {
}
