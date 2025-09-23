package br.com.cultive_api.mapper;

import br.com.cultive_api.dto.request.CreateProductDto;
import br.com.cultive_api.dto.response.ProductResponseDto;
import br.com.cultive_api.entites.Product;
import br.com.cultive_api.entites.ProductCategory;
import br.com.cultive_api.entites.User;

import java.time.LocalDateTime;

public class ProductMapper {

    public static Product toEntity(CreateProductDto dto, User user, ProductCategory category) {
        Product newProduct = new Product();
        newProduct.setName(dto.name());
        newProduct.setDescription(dto.description());
        newProduct.setPrice(dto.price());
        newProduct.setInStock(dto.inStock());
        newProduct.setCategory(category);
        newProduct.setRegistrationDate(LocalDateTime.now());
        newProduct.setAvailable(true);
        newProduct.setImageUrl(dto.imageUrl());
        newProduct.setUser(user);

        return newProduct;
    }

    public static ProductResponseDto toProductRes(Product product) {
        var productId = product.getProductId();
        var name = product.getName();
        var description = product.getDescription();
        var price = product.getPrice();
        var inStocck = product.getInStock();
        var category = product.getCategory();
        var registrationDate = product.getRegistrationDate();
        var lastUpdate = product.getLastUpdate();
        var available = product.isAvailable();
        var imageUrl = product.getImageUrl();
        var idUser = product.getUser().getUserId();
        var userName = product.getUser().getName();
        var userEmail = product.getUser().getEmail();

        return new ProductResponseDto(productId, name, description,
                                      price, inStocck, category,
                                      registrationDate, lastUpdate, available, imageUrl,
                                      idUser, userName, userEmail);
    }

    public static void toUpdate(CreateProductDto dto, Product productUpdate, ProductCategory category) {
        productUpdate.setName(dto.name());
        productUpdate.setDescription(dto.description());
        productUpdate.setPrice(dto.price());
        productUpdate.setInStock(dto.inStock());
        productUpdate.setCategory(category);
        productUpdate.setImageUrl(dto.imageUrl());
        productUpdate.setLastUpdate(LocalDateTime.now());
    }

}
