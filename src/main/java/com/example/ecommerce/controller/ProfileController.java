package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController {

    @Autowired private UserService userService;
    @Autowired private OrderService orderService;
    @Autowired private CartService cartService;

    @GetMapping
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("orders", orderService.getOrdersForUser(user));
        model.addAttribute("cartCount", cartService.getCartForUser(user).getItems().size());
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam String username,
                                @RequestParam String email,
                                HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        user.setUsername(username);
        user.setEmail(email);
        userService.updateUser(user);
        session.setAttribute("user", user);
        model.addAttribute("user", user);
        model.addAttribute("orders", orderService.getOrdersForUser(user));
        model.addAttribute("success", "Profile updated successfully!");
        return "profile";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        User verified = userService.loginUser(user.getEmail(), currentPassword);
        model.addAttribute("user", user);
        model.addAttribute("orders", orderService.getOrdersForUser(user));
        if (verified == null) {
            model.addAttribute("pwError", "Current password is incorrect.");
            return "profile";
        }
        user.setPassword(userService.hashPassword(newPassword));
        userService.updateUser(user);
        session.setAttribute("user", user);
        model.addAttribute("pwSuccess", "Password changed successfully!");
        return "profile";
    }
}