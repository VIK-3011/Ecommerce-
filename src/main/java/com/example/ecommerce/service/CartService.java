package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private ProductRepository productRepository;

    // Get or create cart for user
    public Cart getCartForUser(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        return cart;
    }

    public Cart addToCart(User user, Long productId, int quantity) {
        Cart cart = getCartForUser(user);
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return cart;

        // If product already in cart, increase quantity
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                cartRepository.save(cart);
                return cart;
            }
        }

        // Otherwise add new item
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);
        cart.getItems().add(item);
        cartRepository.save(cart);
        return cart;
    }

    public Cart updateQuantity(User user, Long itemId, int quantity) {
        Cart cart = getCartForUser(user);
        cart.getItems().forEach(item -> {
            if (item.getId().equals(itemId)) {
                item.setQuantity(quantity);
            }
        });
        cartRepository.save(cart);
        return cart;
    }

    public Cart removeItem(User user, Long itemId) {
        Cart cart = getCartForUser(user);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cartRepository.save(cart);
        return cart;
    }

    public void clearCart(User user) {
        Cart cart = getCartForUser(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}