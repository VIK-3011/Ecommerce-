package com.example.ecommerce.controller;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    // ── helpers ──────────────────────────────────────────────────────────────

    private boolean isAdmin(HttpSession session) {
        User u = (User) session.getAttribute("user");
        return u != null && "ADMIN".equals(u.getRole());
    }

    // ── login ─────────────────────────────────────────────────────────────────

    @GetMapping("/login")
    public String adminLoginPage(HttpSession session) {
        if (isAdmin(session)) return "redirect:/admin/dashboard";
        return "admin/login";
    }

    @PostMapping("/login")
    public String adminLogin(@RequestParam String email,
                             @RequestParam String password,
                             HttpSession session, Model model) {
        User user = userService.loginUser(email, password);
        if (user != null && "ADMIN".equals(user.getRole())) {
            session.setAttribute("user", user);
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid admin credentials");
        return "admin/login";
    }

    @GetMapping("/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    // ── dashboard ─────────────────────────────────────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("productCount", productService.getAllProducts().size());
        model.addAttribute("categoryCount", productService.getAllCategories().size());
        return "admin/dashboard";
    }

    // ── products ──────────────────────────────────────────────────────────────

    @GetMapping("/products/new")
    public String newProductForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        model.addAttribute("product", new Product());
        model.addAttribute("categories", productService.getAllCategories());
        return "admin/product-form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam Long categoryId,
                              HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        Category cat = productService.getAllCategories()
                .stream().filter(c -> c.getId().equals(categoryId))
                .findFirst().orElse(null);
        product.setCategory(cat);
        productService.saveProduct(product);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id,
                                  HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", productService.getAllCategories());
        return "admin/product-form";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        productService.deleteProduct(id);
        return "redirect:/admin/dashboard";
    }

    // ── categories ────────────────────────────────────────────────────────────

    @GetMapping("/categories/new")
    public String newCategoryForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        model.addAttribute("category", new Category());
        return "admin/category-form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute Category category, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        productService.saveCategory(category);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategoryForm(@PathVariable Long id,
                                   HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        model.addAttribute("category", productService.getCategoryById(id));
        return "admin/category-form";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin/login";
        productService.deleteCategory(id);
        return "redirect:/admin/dashboard";
    }
}
