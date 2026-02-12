package com.test_shop.cart.controller;

import com.test_shop.cart.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final RuleRepository ruleRepository;
    private final PaymentProcessorRepository processorRepository;

    public AdminHomeController(ProductRepository productRepository, 
                               BrandRepository brandRepository, 
                               RuleRepository ruleRepository, 
                               PaymentProcessorRepository processorRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.ruleRepository = ruleRepository;
        this.processorRepository = processorRepository;
    }

    @GetMapping
    public String dashboard(Model model) {
        // Adding counts to show status on the dashboard
        model.addAttribute("productCount", productRepository.count());
        model.addAttribute("brandCount", brandRepository.count());
        model.addAttribute("ruleCount", ruleRepository.count());
        model.addAttribute("processorCount", processorRepository.count());
        
        return "admin/dashboard";
    }
}