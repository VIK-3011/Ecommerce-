package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class BaseController {

    @Autowired
    private CartService cartService;

    @ModelAttribute("cartCount")
    public int cartCount(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return 0;
        return cartService.getCartForUser(user).getItems().size();
    }
}
