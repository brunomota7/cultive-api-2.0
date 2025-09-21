package br.com.cultive_api.dto.request;

import br.com.cultive_api.entites.ProductCategory;
import br.com.cultive_api.entites.User;

public record CreateProductDto(String name,
                               String description,
                               Double price,
                               Integer inStock,
                               String category,
                               String imageUrl,
                               User user) {
}
