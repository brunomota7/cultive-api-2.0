package br.com.cultive_api.controller;

import br.com.cultive_api.dto.request.CreateProductDto;
import br.com.cultive_api.dto.response.ProductResponseDto;
import br.com.cultive_api.entites.ProductCategory;
import br.com.cultive_api.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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
        productService.createNewProduct(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('SCOPE_FARMER') or hasRole('SCOPE_ADMIN')")
    public ResponseEntity<Page<ProductResponseDto>> getAllMyProducts(
            @PageableDefault(page = 1, size = 5, sort = "name") Pageable pageable,
            JwtAuthenticationToken token
    ) {
        Page<ProductResponseDto> listMyProducts = productService
                .getAllMyProducts(pageable, token);
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
        Page<ProductResponseDto> listProducts = productService
                .filterProductsBy(name, category, minPrice, maxPrice, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listProducts);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        ProductResponseDto product = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('SCOPE_FARMER') or hasRole('SCOPE_ADMIN')")
    public ResponseEntity<Page<ProductResponseDto>> listAvailableProductsTrue(
            @PageableDefault(page = 1, size = 5, sort = "name") Pageable pageable
    ) {
        Page<ProductResponseDto> listProductsAvailableT = productService.listAvailbleProductsTrue(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listProductsAvailableT);
    }

    @GetMapping("/no-available")
    @PreAuthorize("hasRole('SCOPE_FARMER') or hasRole('SCOPE_ADMIN')")
    public ResponseEntity<Page<ProductResponseDto>> listAvailabelProductsFalse(
            @PageableDefault(page = 1, size = 5, sort = "name") Pageable pageable
    ) {
        Page<ProductResponseDto> listProductsAvailableF = productService.listAvailabelProductsFalse(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listProductsAvailableF);
    }

    @PatchMapping("/{productId}/availability")
    @PreAuthorize("hasRole('SCOPE_FARMER') or hasRole('SCOPE_ADMIN')")
    public ResponseEntity<Void> changeAvailability(
            @PathVariable Long productId, @RequestParam boolean available
    ) {
        productService.changeAvailabitity(productId, available);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update/{productId}")
    @PreAuthorize("hasRole('SCOPE_FARMER') or hasRole('SCOPE_ADMIN')")
    public ResponseEntity<Void> updateInfoProduct(
            @PathVariable Long productId,
            @RequestBody CreateProductDto dto
    ) {
        productService.updateInfoProduct(productId,dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasRole('SCOPE_FARMER') or hasRole('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
