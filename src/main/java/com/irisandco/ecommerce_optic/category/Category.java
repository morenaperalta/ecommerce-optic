package com.irisandco.ecommerce_optic.category;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_category")
    private Long id;

    @Column(name="name", table="categories", insertable=true, updatable=true, nullable=false, columnDefinition = "VARCHAR(255) NOT NULL UNIQUE")
    private String name;

    @ManyToMany
    @JoinTable(
            name="category_product",
            joinColumns = @JoinColumn(name="id_category"),
            inverseJoinColumns = @JoinColumn(name="id_product"))
    private List<Product> products;

    public Category(){}

    public Category(Long id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    //public void setId(Long id) {
    //    this.id = id;
    //}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
