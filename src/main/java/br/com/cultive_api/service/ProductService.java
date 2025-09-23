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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
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

        log.info("Starting the process of adding a new product: {}", dto.name());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        ProductCategory category = productCategoryRepository.findByName(dto.category());
        if (category == null) {
            log.error("Invalid category for registration: {}", dto.category());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category for registration");
        }

        Product newProduct = ProductMapper.toEntity(dto, user, category);

        productRepository.save(newProduct);

        log.info("Product '{}' added successfully. ID: {}", dto.name(), newProduct.getProductId());
    }

    public Page<ProductResponseDto> getAllMyProducts(Pageable pageable, JwtAuthenticationToken token) {
        var userId = AppUtils.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        log.info("User ID '{}' started a search for your products", user.getUserId());

        return productRepository.findAllByUserUserId(user.getUserId(), pageable)
                .map(ProductMapper::toProductRes);
    }

    public Page<ProductResponseDto> filterProductsBy(
            String name, ProductCategory category, Double minPrice, Double maxPrice, Pageable pageable
    ) {
        log.info("Starting product filtering process with parameters: name={}, category={}, minPrice={}, maxPrice={}",
                name, category, minPrice, maxPrice);

        Page<ProductResponseDto> result = productRepository
                .searchProducts(name, category, minPrice, maxPrice, pageable)
                .map(ProductMapper::toProductRes);

        log.info("Product filtering completed successfully. Found {} products", result.getTotalElements());

        return result;
    }

    public ProductResponseDto getProductById(Long productId) {
        log.info("Starting the search for product with ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with ID '{}' not found", productId);
                    return new ProductNotFoundException("Product not found");
                });

        log.info("Product with ID '{}' found successfully: {}", productId, product.getName());

        return ProductMapper.toProductRes(product);
    }

    public Page<ProductResponseDto> listAvailbleProductsTrue(Pageable pageable) {
        log.info("Starting the search for available products (available = true)");

        Page<ProductResponseDto> listProduct = productRepository
                .findByAvailableTrue(pageable)
                .map(ProductMapper::toProductRes);

        log.info("Search for available products completed. Found {} products", listProduct.getTotalElements());

        return listProduct;
    }

    public Page<ProductResponseDto> listAvailabelProductsFalse(Pageable pageable) {
        log.info("Starting the search for unavailable products (available = false)");

        Page<ProductResponseDto> listProduct = productRepository
                .findByAvailableFalse(pageable)
                .map(ProductMapper::toProductRes);

        log.info("Search for unavailable products completed. Found {} products", listProduct.getTotalElements());

        return listProduct;
    }

    @Transactional
    public void changeAvailabitity(Long productId, boolean available) {
        log.info("Starting the process of changing the availability of product with ID '{}' to '{}'", productId, available);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with ID '{}' not found", productId);
                    return new ProductNotFoundException("Product not found");
                });

        product.setAvailable(available);
        productRepository.save(product);

        log.info("Product with ID '{}' successfully updated its availability to '{}'", productId, available);
    }

    @Transactional
    public void updateInfoProduct(Long productId, CreateProductDto dto) {
        log.info("Starting the process of updating product information for ID '{}'", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with ID '{}' not found for update", productId);
                    return new ProductNotFoundException("Product not found");
                });

        ProductCategory category = productCategoryRepository.findByName(dto.category());
        if (category == null) {
            log.error("Invalid category for update: {}", dto.category());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category for registration");
        }

        ProductMapper.toUpdate(dto, product, category);
        productRepository.save(product);

        log.info("Product with ID '{}' successfully updated", productId);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        log.warn("Starting the process of deleting product with ID '{}'", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with ID '{}' not found for deletion", productId);
                    return new ProductNotFoundException("Product not found");
                });

        productRepository.delete(product);

        log.warn("Product with ID '{}' successfully deleted", productId);
    }

}