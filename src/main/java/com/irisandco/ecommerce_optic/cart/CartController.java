package com.irisandco.ecommerce_optic.cart;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService CART_SERVICE;

    public CartController(CartService cartService) {
        CART_SERVICE = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId){
        CartResponse cartResponse = CART_SERVICE.getCartResponseByUserId(userId);
        return ResponseEntity.ok(cartResponse);
    }

    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<Object> addItemToCart(@PathVariable Long userId, @PathVariable Long productId, @Valid @RequestBody CartRequest cartRequest){
        List<String> productName = CART_SERVICE.addItemToCart(userId, productId, cartRequest);
        return ResponseEntity.ok().body("New product added to cart: \"" + productName.get(0) + "\" " + productName.get(1) + " units");

    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<Object> removeItemFromCart(@PathVariable Long userId, @PathVariable Long productId){
        String productName =  CART_SERVICE.removeItemFromCart(userId, productId);
        return ResponseEntity.ok().body("Product \"" + productName + "\" deleted from cart");

    }
}
