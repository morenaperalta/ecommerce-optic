package com.irisandco.ecommerce_optic.category;

import com.irisandco.ecommerce_optic.product.Product;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", table="categories", nullable=false, unique = true, length = 50)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<Product> products = new ArrayList<>();

    public Category(){}

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }
}
