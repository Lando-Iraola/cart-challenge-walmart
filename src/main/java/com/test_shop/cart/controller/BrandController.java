package com.test_shop.cart.controller;

import com.test_shop.cart.model.Brand;
import com.test_shop.cart.repository.BrandRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/brands")
public class BrandController {

    private final BrandRepository brandRepository;

    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    // 1. Show the form and the list of existing brands
    @GetMapping("/new")
    public String showBrandForm(Model model) {
        model.addAttribute("brand", new Brand()); // For the form binding
        model.addAttribute("brands", brandRepository.findAll()); // To show existing brands below the form
        return "brand-form";
    }

    // 2. Save the new brand
    @PostMapping("/save")
    public String saveBrand(@ModelAttribute("brand") Brand brand) {
        brandRepository.save(brand);
        return "redirect:/brands/new?success";
    }
}