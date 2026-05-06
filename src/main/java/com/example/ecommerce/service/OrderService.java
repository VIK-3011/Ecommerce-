package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private CartService cartService;

    public Order placeOrder(User user, String address) {
        Cart cart = cartService.getCartForUser(user);
        if (cart.getItems().isEmpty()) return null;

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setTotalAmount(cart.getTotal());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(cartItem.getProduct());
            oi.setQuantity(cartItem.getQuantity());
            oi.setPrice(cartItem.getProduct().getPrice());
            orderItems.add(oi);
        }

        order.setItems(orderItems);
        orderRepository.save(order);

        // Clear cart after order placed
        cartService.clearCart(user);
        return order;
    }

    public List<Order> getOrdersForUser(User user) {
        return orderRepository.findByUserOrderByPlacedAtDesc(user);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}