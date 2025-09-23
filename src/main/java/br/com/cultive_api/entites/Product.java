package br.com.cultive_api.entites;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    private String name;
    private String description;
    private Double price;
    private Integer inStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;

    private LocalDateTime registrationDate;
    private boolean available;
    private String imageUrl;
    private LocalDateTime lastUpdate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getProductId() {return productId;}
    public void setProductId(Long productId) {this.productId = productId;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Double getPrice() {return price;}
    public void setPrice(Double price) {this.price = price;}

    public Integer getInStock() {return inStock;}
    public void setInStock(Integer inStock) {this.inStock = inStock;}

    public ProductCategory getCategory() {return category;}
    public void setCategory(ProductCategory category) {this.category = category;}

    public LocalDateTime getRegistrationDate() {return registrationDate;}
    public void setRegistrationDate(LocalDateTime registrationDate) {this.registrationDate = registrationDate;}

    public LocalDateTime getLastUpdate() {return lastUpdate;}
    public void setLastUpdate(LocalDateTime lastUpdate) {this.lastUpdate = lastUpdate;}

    public boolean isAvailable() {return available;}
    public void setAvailable(boolean available) {this.available = available;}

    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
}
