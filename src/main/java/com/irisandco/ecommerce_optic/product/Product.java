package com.irisandco.ecommerce_optic.product;

import com.irisandco.ecommerce_optic.category.Category;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long  id;

    @Column(name="name", table="products", nullable=false,  unique = true, length = 50)
    private String name;

    @Column(name="price", table="products")
    private Double price;

    @Column(name="imageUrl", table="products")
    private String imageUrl;

    @Column(name="featured", table="products")
    private Boolean featured;

    @ManyToMany
    @JoinTable(
            name="category_product",
            joinColumns = @JoinColumn(name="product_id"),
            inverseJoinColumns = @JoinColumn(name="category_id"))
    private List<Category> categories;

    public Product() {
    }

    public Product(Long id, String name, Double price, String imageUrl, Boolean featured, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.featured = featured;
        this.categories = categories;
    }

    public Product(String name, Double price, String imageUrl, Boolean featured, List<Category> categories) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.featured = featured;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
