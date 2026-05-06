package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("cart", cartService.getCartForUser(user));
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam int quantity,
                            HttpSession session) {
        User user = (User) session.getAttribute("user");
        cartService.addToCart(user, productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam Long itemId,
                             @RequestParam int quantity,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (quantity <= 0) {
            cartService.removeItem(user, itemId);
        } else {
            cartService.updateQuantity(user, itemId, quantity);
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long itemId,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        cartService.removeItem(user, itemId);
        return "redirect:/cart";
    }
}