package br.com.cultive_api.dto.response;

import br.com.cultive_api.entites.ProductCategory;
import br.com.cultive_api.entites.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDto(Long productId,
                                 String name,
                                 String description,
                                 Double price,
                                 Integer inStock,
                                 ProductCategory category,
                                 LocalDateTime registrationDate,
                                 boolean available,
                                 String imageUrl,
                                 UUID userId,
                                 String userName,
                                 String userEmail) {
}
