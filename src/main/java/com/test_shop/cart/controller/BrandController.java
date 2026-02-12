package com.test_shop.cart.controller;

import com.test_shop.cart.model.Brand;
import com.test_shop.cart.repository.BrandRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/brands")
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
        return "admin/brand-form";
    }

    // 2. Save the new brand
    @PostMapping("/save")
    public String saveBrand(@ModelAttribute("brand") Brand brand, RedirectAttributes redirectAttributes) {
        // Check if name already exists
        if (brandRepository.existsByName(brand.getName())) {
            // Use flash attributes to pass a message through the redirect
            redirectAttributes.addFlashAttribute("error", "The brand '" + brand.getName() + "' already exists!");
            return "redirect:/admin/brands/new";
        }

        brandRepository.save(brand);
        return "redirect:/admin/brands/new?success";
    }
}