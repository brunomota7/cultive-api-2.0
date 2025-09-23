package br.com.cultive_api.service;

import br.com.cultive_api.dto.request.CreateProductDto;
import br.com.cultive_api.dto.response.ProductResponseDto;
import br.com.cultive_api.entites.Product;
import br.com.cultive_api.entites.ProductCategory;
import br.com.cultive_api.entites.User;
import br.com.cultive_api.exception.ProductNotFoundException;
import br.com.cultive_api.exception.UserNotFoundException;
import br.com.cultive_api.mapper.ProductMapper;
import br.com.cultive_api.repository.ProductCategoryRepository;
import br.com.cultive_api.repository.ProductRepository;
import br.com.cultive_api.repository.UserRepository;
import br.com.cultive_api.utils.AppUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private UserRepository userRepository;
    private ProductCategoryRepository productCategoryRepository;

    public ProductService(ProductRepository productRepository,
                          UserRepository userRepository,
                          ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @Transactional
    public void createNewProduct(CreateProductDto dto, JwtAuthenticationToken token) {
        UUID userId = AppUtils.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        ProductCategory category = productCategoryRepository.findByName(dto.category());
        if (category == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category for registration");

        Product newProduct = ProductMapper.toEntity(dto, user, category);

        productRepository.save(newProduct);
    }

    public Page<ProductResponseDto> getAllMyProducts(Pageable pageable, JwtAuthenticationToken token) {
        var userId = AppUtils.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return productRepository.findAllByUserUserId(user.getUserId(), pageable)
                .map(ProductMapper::toProductRes);
    }

    public Page<ProductResponseDto> filterProductsBy(
            String name, ProductCategory category, Double minPrice, Double maxPrice, Pageable pageable
    ) {
        Page<ProductResponseDto> result = productRepository
                .searchProducts(name, category, minPrice, maxPrice, pageable)
                .map(ProductMapper::toProductRes);

        return result;
    }

    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        return ProductMapper.toProductRes(product);
    }

    public Page<ProductResponseDto> listAvailbleProductsTrue(Pageable pageable) {
        Page<ProductResponseDto> listProduct = productRepository
                .findByAvailableTrue(pageable)
                .map(ProductMapper::toProductRes);

        return listProduct;
    }

    public Page<ProductResponseDto> listAvailabelProductsFalse(Pageable pageable) {
        Page<ProductResponseDto> listProduct = productRepository
                .findByAvailableFalse(pageable)
                .map(ProductMapper::toProductRes);

        return listProduct;
    }

    public void changeAvailabitity(Long productId, boolean available) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        product.setAvailable(available);
        productRepository.save(product);
    }

    public void updateInfoProduct(Long productId, CreateProductDto dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        ProductCategory category = productCategoryRepository.findByName(dto.category());
        if (category == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category for registration");

        ProductMapper.toUpdate(dto, product, category);
        productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        productRepository.delete(product);
    }

}