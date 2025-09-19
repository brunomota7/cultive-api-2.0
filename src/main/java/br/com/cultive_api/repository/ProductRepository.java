package br.com.cultive_api.repository;

import br.com.cultive_api.entites.Product;
import br.com.cultive_api.entites.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByUserUserId(UUID userId);
    List<Product> findByAvailableTrue();
    List<Product> findByAvailableFalse();

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND" +
            "(:category IS NULL OR p.category = :category) AND" +
            "(:priceMin IS NULL OR p.price >= :priceMin) AND" +
            "(:priceMax IS NULL OR p.price <= :priceMax)"
    )
    List<Product> searchProducts(
            @Param("name") String name,
            @Param("category") ProductCategory category,
            @Param("priceMin") Double priceMin,
            @Param("priceMax") Double priceMax
    );

}
