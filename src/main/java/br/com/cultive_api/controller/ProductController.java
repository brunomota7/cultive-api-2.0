package br.com.cultive_api.controller;

import br.com.cultive_api.dto.request.CreateProductDto;
import br.com.cultive_api.dto.response.ProductResponseDto;
import br.com.cultive_api.entites.ProductCategory;
import br.com.cultive_api.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SCOPE_FARMER') or hasRole('SCOPE_ADMIN')")
    public ResponseEntity<Void> createNewProduct(@RequestBody CreateProductDto dto,
                                                 JwtAuthenticationToken token) {
        log.info("POST /product - Starting product creation request");
        productService.createNewProduct(dto, token);
        log.info("POST /product - Product created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('SCOPE_FARMER') or hasRole('SCOPE_ADMIN')")
    public ResponseEntity<Page<ProductResponseDto>> getAllMyProducts(
            @PageableDefault(page = 1, size = 5, sort = "name") Pageable pageable,
            JwtAuthenticationToken token
    ) {
        log.info("GET /product/my - Starting request to list all products for the logged-in user");
        Page<ProductResponseDto> listMyProducts = productService
                .getAllMyProducts(pageable, token);
        log.info("GET /product/my - Found {} products for the user", listMyProducts.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(listMyProducts);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> filterProductsBy(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @PageableDefault(page = 1, size = 5, sort = "name")Pageable pageable
    ) {
        log.info("GET /product - Starting product filtering request with parameters: name={}, category={}, minPrice={}, maxPrice={}",
                name, category, minPrice, maxPrice);
        Page<ProductResponseDto> listProducts = productService
                .filterProductsBy(name, category, minPrice, maxPrice, pageable);
        log.info("GET /product - Product filtering completed. Found {} products", listProducts.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(listProducts);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        log.info("GET /product/{} - Starting request to get product by ID", productId);
        ProductResponseDto product = productService.getProductById(productId);
        log.info("GET /product/{} - Product found successfully", productId);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('SCOPE_FARMER') or hasRole('SCOPE_ADMIN')")
    public ResponseEntity<Page<ProductResponseDto>> listAvailableProductsTrue(
            @PageableDefault(page = 1, size = 5, sort = "name") Pageable pageable
    ) {
        log.info("GET /product/available - Starting request to list all available products");
        Page<ProductResponseDto> listProductsAvailableT = productService.listAvailbleProductsTrue(pageable);
        log.info("GET /product/available - Found {} available products", listProductsAvailableT.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(listProductsAvailableT);
    }

    @GetMapping("/no-available")
    @PreAuthorize("hasRole('SCOPE_FARMER') or hasRole('SCOPE_ADMIN')")
    public ResponseEntity<Page<ProductResponseDto>> listAvailabelProductsFalse(
            @PageableDefault(page = 1, size = 5, sort = "name") Pageable pageable
    ) {
        log.info("GET /product/no-available - Starting request to list all unavailable products");
        Page<ProductResponseDto> listProductsAvailableF = productService.listAvailabelProductsFalse(pageable);
        log.info("GET /product/no-available - Found {} unavailable products", listProductsAvailableF.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(listProductsAvailableF);
    }

    @PatchMapping("/{productId}/availability")
    @PreAuthorize("hasRole('SCOPE_FARMER') or hasRole('SCOPE_ADMIN')")
    public ResponseEntity<Void> changeAvailability(
            @PathVariable Long productId, @RequestParam boolean available
    ) {
        log.info("PATCH /product/{}/availability - Starting request to change availability to {}", productId, available);
        productService.changeAvailabitity(productId, available);
        log.info("PATCH /product/{}/availability - Product availability updated successfully", productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update/{productId}")
    @PreAuthorize("hasRole('SCOPE_FARMER') or hasRole('SCOPE_ADMIN')")
    public ResponseEntity<Void> updateInfoProduct(
            @PathVariable Long productId,
            @RequestBody CreateProductDto dto
    ) {
        log.info("PUT /product/update/{} - Starting request to update product information", productId);
        productService.updateInfoProduct(productId,dto);
        log.info("PUT /product/update/{} - Product information updated successfully", productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasRole('SCOPE_FARMER') or hasRole('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        log.warn("DELETE /product/delete/{} - Starting request to delete product", productId);
        productService.deleteProduct(productId);
        log.warn("DELETE /product/delete/{} - Product deleted successfully", productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}