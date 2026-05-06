package com.example.ecommerce.controller;

import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController extends BaseController {

    @Autowired
    private ProductService productService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("featuredProducts", productService.getAllProducts());
        model.addAttribute("categories", productService.getAllCategories());
        return "home";
    }
}