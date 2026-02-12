package com.test_shop.cart.controller;

import com.test_shop.cart.model.PaymentProcessor;
import com.test_shop.cart.repository.PaymentProcessorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/processors")
public class PaymentProcessorController {

    private final PaymentProcessorRepository processorRepository;

    public PaymentProcessorController(PaymentProcessorRepository processorRepository) {
        this.processorRepository = processorRepository;
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("processor", new PaymentProcessor());
        model.addAttribute("processors", processorRepository.findAll());
        return "processor-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("processor") PaymentProcessor processor) {
        processorRepository.save(processor);
        return "redirect:/processors/new?success";
    }
}