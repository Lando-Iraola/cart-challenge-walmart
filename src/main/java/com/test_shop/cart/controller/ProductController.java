package com.test_shop.cart.controller;

import com.test_shop.cart.model.Product;
import com.test_shop.cart.repository.ProductRepository;
import com.test_shop.cart.repository.BrandRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    public ProductController(ProductRepository productRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
    }

    @GetMapping("/new")
    public String showProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("brands", brandRepository.findAll());
        // Added: Fetch all products to show in the side table
        model.addAttribute("products", productRepository.findAll()); 
        return "product-form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productRepository.save(product);
        return "redirect:/products/new?success";
    }
}