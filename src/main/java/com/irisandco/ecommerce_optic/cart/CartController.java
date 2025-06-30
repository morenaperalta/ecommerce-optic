package com.irisandco.ecommerce_optic.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService CART_SERVICE;

    public CartController(CartService cartService) {
        CART_SERVICE = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(CART_SERVICE.getCartResponseByUserId(userId));
    }

    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<Object> addItemToCart(@PathVariable Long userId, @PathVariable Long productId, @RequestBody CartRequest cartRequest){
        CART_SERVICE.addItemToCart(userId, productId, cartRequest);
        return ResponseEntity.ok().body("New product added to cart");

    }

    @DeleteMapping("/api/cart/{userId}/remove/{productId}")
    public ResponseEntity<Object> removeItemFromCart(@PathVariable Long userId, @PathVariable Long productId){
        CART_SERVICE.removeItemFromCart(userId, productId);
        return ResponseEntity.ok().body("Product deleted from cart");

    }
}
