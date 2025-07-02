package com.irisandco.ecommerce_optic.item;

import com.irisandco.ecommerce_optic.product.Product;
import com.irisandco.ecommerce_optic.cart.Cart;
import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="quantity", table="items", nullable = false)
    private Integer quantity;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    @ManyToOne(targetEntity = Cart.class)
    @JoinColumn(name="cart_id", nullable = false)
    private Cart cart;

    public Item() {
    }

    public Item newItem(int quantity, Product product, Cart cart) {
        Item item = new Item(quantity, product, cart);
        cart.getItems().add(item);
        return item;
    }

    public Item(int quantity, Product product, Cart cart) {
        this.quantity = quantity;
        this.product = product;
        this.cart = cart;;
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
