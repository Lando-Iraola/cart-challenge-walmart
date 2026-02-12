package com.test_shop.cart.controller;

import com.test_shop.cart.dto.CartRequest;
import com.test_shop.cart.model.Product;
import com.test_shop.cart.model.rules.RuleEntity;
import com.test_shop.cart.model.Cart;
import com.test_shop.cart.model.CartItem;
import com.test_shop.cart.model.PaymentProcessor;
import com.test_shop.cart.repository.ProductRepository;
import com.test_shop.cart.repository.PaymentProcessorRepository;
import com.test_shop.cart.repository.RuleRepository; // 1. ADDED IMPORT
import com.test_shop.cart.service.CartCalculationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductRepository productRepository;
    private final PaymentProcessorRepository processorRepository;
    private final CartCalculationService calculationService;
    private final RuleRepository ruleRepository; // 2. ADDED FIELD

    // 3. UPDATED CONSTRUCTOR TO INCLUDE RULE REPOSITORY
    public CartController(ProductRepository productRepository,
                          PaymentProcessorRepository processorRepository,
                          CartCalculationService calculationService,
                          RuleRepository ruleRepository) {
        this.productRepository = productRepository;
        this.processorRepository = processorRepository;
        this.calculationService = calculationService;
        this.ruleRepository = ruleRepository; // 4. ASSIGNED FIELD
    }

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartRequest", new CartRequest());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("processors", processorRepository.findAll());
        return "cart-view";
    }

    @PostMapping("/calculate")
    public String calculateTotal(@ModelAttribute CartRequest cartRequest, Model model) {
        Product product = productRepository.findById(cartRequest.getProductId()).orElseThrow();
        PaymentProcessor processor = processorRepository.findById(cartRequest.getProcessorId()).orElseThrow();

        Cart tempCart = new Cart();
        tempCart.setPaymentProcessor(processor);
        tempCart.setTaxRate("0.19");

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(cartRequest.getQuantity());
        tempCart.setItems(List.of(item));

        String finalPriceDisplay = calculationService.calcularTotal(tempCart);

        // This line now has access to the ruleRepository field
        List<RuleEntity> appliedRules = ruleRepository.findAllByOrderByWeightDesc().stream()
                .filter(rule -> rule.isEligible(product, processor))
                .toList();

        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("processors", processorRepository.findAll());
        model.addAttribute("selectedProduct", product);
        model.addAttribute("selectedProcessor", processor);
        model.addAttribute("selectedQuantity", cartRequest.getQuantity());
        model.addAttribute("appliedRules", appliedRules);
        model.addAttribute("taxRate", "19%");
        model.addAttribute("finalPrice", finalPriceDisplay);

        return "cart-view";
    }
}