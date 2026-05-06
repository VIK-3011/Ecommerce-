package com.example.ecommerce.controller;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController extends BaseController {

    @Autowired private OrderService orderService;
    @Autowired private CartService cartService;

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("cart", cartService.getCartForUser(user));
        return "checkout";
    }

    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam String address,
                             HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        Order order = orderService.placeOrder(user, address);
        if (order == null) return "redirect:/cart";
        model.addAttribute("order", order);
        return "order-success";
    }

    @GetMapping("/orders")
    public String orderHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("orders", orderService.getOrdersForUser(user));
        return "order-history";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "order-detail";
    }
}