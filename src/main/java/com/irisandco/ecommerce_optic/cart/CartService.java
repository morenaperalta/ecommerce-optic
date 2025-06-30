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

import java.util.Collections;
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

//    public Cart getCartById(Long id){
//        return CART_REPOSITORY.findById(id).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
//
//    }

    public Cart getCartByUserId(Long id){
        return CART_REPOSITORY.findCartByUserId(id).orElseThrow(() -> new IllegalArgumentException("Cart not found for this user ID"));

    }

    public CartResponse getCartResponseByUserId(Long id){
        return toDto(getCartByUserId(id));

    }

    public Cart getCartOrCreateByUserId(Long id){
        return CART_REPOSITORY.findCartByUserId(id).orElse(createCart(id));

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

//    public CartResponse createCartResponse(Long userId){
//        Cart cart = createCart(userId);
//        return CartMapper.toDto(CART_REPOSITORY.save(cart), List.of());
//    }

    public void addItemToCart(Long userId, Long productId, CartRequest cartRequest){
        Cart cart = getCartOrCreateByUserId(userId);
        Product product = PRODUCT_SERVICE.getProductById(productId);
        List<ItemResponse> itemsResponse;
        if(cart.getItems() != null && !cart.getItems().isEmpty()) {
            itemsResponse = cart.getItems().stream()
                    .map((item) -> {
                                if (Objects.equals(product.getId(), item.getProduct().getId())) {
                                    ITEM_SERVICE.updateItem(item, cartRequest.quantity()) ;
                                    return ItemMapper.toDto(item, product);
                                } else {
                                    return ItemMapper.toDto(item, item.getProduct());
                                }
                            }
                    ).toList();
        } else {
            Item newItem = new Item(cartRequest.quantity(), product, cart);
            ITEM_SERVICE.createItem(newItem);
            cart.getItems().add(newItem);
            itemsResponse = Collections.singletonList(ItemMapper.toDto(newItem, product));
        }
        updateCartPrice(cart);
        CartMapper.toDto(CART_REPOSITORY.save(cart), itemsResponse);
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
                .map((item) -> ItemMapper.toDto(item, product))
                .toList();

        updateCartPrice(cart);
        CartMapper.toDto(CART_REPOSITORY.save(cart), itemsResponse);
    }

//    public void deleteCart(Long id){
//        Cart cart = getCartById(id);
//        CART_REPOSITORY.deleteById(id);
//    }

//    private List<CartResponse> listToDto(List<Cart> carts) {
//        return carts.stream()
//                .map(this::toDto)
//                .toList();
//    }

    private CartResponse toDto(Cart cart) {
        List <ItemResponse> itemsResponse = cart.getItems().stream()
                .map((item) -> ItemMapper.toDto(item, item.getProduct()))
                .toList();
        return CartMapper.toDto(cart, itemsResponse);
    }

    private void updateCartPrice(Cart cart){
        double totalPrice = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice() * item.getQuantity())
                .reduce(0.0, Double::sum);
        cart.setTotalPrice(totalPrice);
    }
}
