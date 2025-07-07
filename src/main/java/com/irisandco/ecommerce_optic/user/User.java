package com.irisandco.ecommerce_optic.user;

import com.irisandco.ecommerce_optic.cart.Cart;
import jakarta.persistence.*;

@Entity
@Table(name= "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", table="users", nullable=false, unique = true, length = 50)
    private String username;

    @Column(name = "email", table="users", nullable=false, unique = true, length = 50)
    private String email;

    @Column(name = "password", table="users", nullable=false, length = 50)
    private String password;

    @OneToOne(mappedBy = "user")
    private Cart cart;

    public User() {
    }

    public User(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void addCart(Cart cart) {
        cart.setUser(this);
        this.cart = cart;
    }
}
