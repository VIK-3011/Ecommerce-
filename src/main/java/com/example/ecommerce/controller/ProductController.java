package com.example.ecommerce.controller;

import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @GetMapping("/shop")
    public String shop(@RequestParam(required = false) Long categoryId,
                       @RequestParam(required = false) String keyword,
                       Model model) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("products", productService.searchProducts(keyword));
        } else if (categoryId != null) {
            model.addAttribute("products", productService.getProductsByCategory(categoryId));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("keyword", keyword);
        return "shop";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product-detail";
    }
}