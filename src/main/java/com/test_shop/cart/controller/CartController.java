package com.test_shop.cart.controller;

import com.test_shop.cart.dto.CalculationResult;
import com.test_shop.cart.dto.CartRequest;
import com.test_shop.cart.model.Cart;
import com.test_shop.cart.model.CartItem;
import com.test_shop.cart.model.PaymentProcessor;
import com.test_shop.cart.model.Product;
import com.test_shop.cart.repository.PaymentProcessorRepository;
import com.test_shop.cart.repository.ProductRepository;
import com.test_shop.cart.repository.RuleRepository;
import com.test_shop.cart.service.CartCalculationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductRepository productRepository;
    private final PaymentProcessorRepository processorRepository;
    private final CartCalculationService calculationService;
    private final RuleRepository ruleRepository;

    /**
     * Constructor-based Dependency Injection.
     * Spring Boot automatically plugs in the required beans.
     */
    public CartController(ProductRepository productRepository,
                          PaymentProcessorRepository processorRepository,
                          CartCalculationService calculationService,
                          RuleRepository ruleRepository) {
        this.productRepository = productRepository;
        this.processorRepository = processorRepository;
        this.calculationService = calculationService;
        this.ruleRepository = ruleRepository;
    }

    /**
     * Shows the initial empty simulator state.
     */
    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartRequest", new CartRequest());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("processors", processorRepository.findAll());
        return "cart-view";
    }

    /**
     * Processes the simulator form, runs the rule engine, and displays results.
     */
    @PostMapping("/calculate")
    public String calculateTotal(@ModelAttribute CartRequest cartRequest, Model model) {
        // 1. Retrieve the selected entities from Database
        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        PaymentProcessor processor = processorRepository.findById(cartRequest.getProcessorId())
                .orElseThrow(() -> new RuntimeException("Payment Processor not found"));

        // 2. Build the temporary Cart object for the Service
        Cart tempCart = new Cart();
        tempCart.setPaymentProcessor(processor);
        tempCart.setTaxRate("0.19"); // Standard 19% tax rate

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(cartRequest.getQuantity());
        tempCart.setItems(List.of(item));

        // 3. Execute the Rule Engine logic via the Service
        // This returns the detailed DTO instead of a simple String
        CalculationResult result = calculationService.calcularTotal(tempCart);

        // 4. Populate the Model for the View
        // Repopulate dropdowns
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("processors", processorRepository.findAll());
        
        // Pass selections back for display
        model.addAttribute("selectedProduct", product);
        model.addAttribute("selectedProcessor", processor);
        model.addAttribute("selectedQuantity", cartRequest.getQuantity());
        
        // Pass calculated details from the DTO
        model.addAttribute("appliedRules", result.getDetails());
        model.addAttribute("taxAmount", result.getTaxAmount());
        model.addAttribute("subtotal", result.getSubtotal());
        model.addAttribute("taxRate", "19%");
        model.addAttribute("finalPrice", result.getFinalTotal());

        return "cart-view";
    }
}