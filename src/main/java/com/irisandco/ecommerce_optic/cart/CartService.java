package com.irisandco.ecommerce_optic.cart;

import com.irisandco.ecommerce_optic.item.Item;
import com.irisandco.ecommerce_optic.item.ItemMapper;
import com.irisandco.ecommerce_optic.item.ItemResponse;
import com.irisandco.ecommerce_optic.item.ItemService;
import com.irisandco.ecommerce_optic.product.Product;
import com.irisandco.ecommerce_optic.product.ProductService;
import com.irisandco.ecommerce_optic.user.User;
import com.irisandco.ecommerce_optic.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CartService {
    private final CartRepository CART_REPOSITORY;
    private final UserService USER_SERVICE;
    private final ProductService PRODUCT_SERVICE;
    private final ItemService ITEM_SERVICE;

    public CartService(CartRepository cartRepository, UserService userService, ProductService productService, ItemService itemService) {
        CART_REPOSITORY = cartRepository;
        USER_SERVICE = userService;
        PRODUCT_SERVICE = productService;
        ITEM_SERVICE = itemService;
    }

    public Cart getCartByUserId(Long id){
        return CART_REPOSITORY.findCartByUserId(id).orElseThrow(() -> new IllegalArgumentException("Cart not found for this user ID"));
    }

    public CartResponse getCartResponseByUserId(Long id){
        return CartMapper.toDto(getCartByUserId(id));
    }

    public Cart getCartOrCreateByUserId(Long userId){
        return CART_REPOSITORY.findCartByUserId(userId)
                .orElseGet(() -> createCart(userId));
        }

    public Cart createCart(Long userId){
        User user = USER_SERVICE.getUserById(userId);
        if (CART_REPOSITORY.existsByUserId(userId)) {
            throw new IllegalArgumentException("There is already a cart for this user " + user.getUsername());
        }
        Cart cart = new Cart();
        cart.setUser(user);
        return CART_REPOSITORY.save(cart);
    }

    public void addItemToCart(Long userId, Long productId, CartRequest cartRequest){
        Cart cart = getCartOrCreateByUserId(userId);
        Product product = PRODUCT_SERVICE.getProductById(productId);
        int quantity = (cartRequest.quantity() != null) ? cartRequest.quantity() : 1;

        cart.getItems().stream()
                .filter(item -> Objects.equals(product, item.getProduct()))
                .findFirst()
                .ifPresentOrElse(
                        item -> ITEM_SERVICE.updateItem(item, quantity),
                        () -> {
                            Item newItem = new Item(quantity, product, cart);
                            ITEM_SERVICE.createItem(newItem);
                        }
                );


//        if (!cart.getItems().isEmpty()) {
//            boolean itemUpdatedOrAdded = cart.getItems().stream()
//                    .anyMatch(item -> {
//                        if (Objects.equals(product, item.getProduct())) {
//                            ITEM_SERVICE.updateItem(item, cartRequest.quantity());
//                            return true; // Found and updated the existing item
//                        }
//                        return false; // Not the matching item
//                    });
//
//            // If no existing item matched, create a new one
//            if (!itemUpdatedOrAdded) {
//                Item newItem = new Item(cartRequest.quantity(), product, cart);
//                ITEM_SERVICE.createItem(newItem);
//            }
//        } else {
//            // Cart is empty, just add a new item
//            Item newItem = new Item(cartRequest.quantity(), product, cart);
//            ITEM_SERVICE.createItem(newItem);
//        }

        updateCartPrice(cart);
        CartMapper.toDto(CART_REPOSITORY.save(cart));
    }

    public void removeItemFromCart(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);
        Product product = PRODUCT_SERVICE.getProductById(productId);

        Item itemToRemove = cart.getItems().stream()
                .filter((item) -> item.getProduct() == product)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found in this cart"));

        cart.getItems().remove(itemToRemove);
        ITEM_SERVICE.deleteItemById(itemToRemove.getId());

        List<ItemResponse> itemsResponse = cart.getItems().stream()
                .map((item) -> ItemMapper.toDto(item))
                .toList();

        updateCartPrice(cart);
        CartMapper.toDto(CART_REPOSITORY.save(cart));
    }

    private void updateCartPrice(Cart cartToUpdate){
        Cart cart = getCartByUserId(cartToUpdate.getUser().getId());
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        cartToUpdate.setTotalPrice(totalPrice);
    }

}
