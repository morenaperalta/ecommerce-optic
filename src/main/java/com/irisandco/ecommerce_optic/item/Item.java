package com.irisandco.ecommerce_optic.item;

import com.irisandco.ecommerce_optic.product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="quantity", table="items")
    private Integer quantity;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(targetEntity = Cart.class)
    @JoinColumn(name="cart_id")
    private Cart cart;

    public Item() {
    }

    public Item(int quantity, Product product, Cart cart) {
        this.quantity = quantity;
        this.product = product;
        this.cart = cart;
    }

    public Item(Long id, int quantity, Product product, Cart cart) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.cart = cart;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
