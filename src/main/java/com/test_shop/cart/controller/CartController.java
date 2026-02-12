package com.test_shop.cart.controller;

import com.test_shop.cart.dto.CartRequest;
import com.test_shop.cart.model.Product;
import com.test_shop.cart.model.Cart;
import com.test_shop.cart.model.CartItem;
import com.test_shop.cart.model.PaymentProcessor;
import com.test_shop.cart.repository.ProductRepository;
import com.test_shop.cart.repository.PaymentProcessorRepository;
import com.test_shop.cart.service.CartCalculationService; // Assuming you have this service
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

    public CartController(ProductRepository productRepository,
            PaymentProcessorRepository processorRepository,
            CartCalculationService calculationService) {
        this.productRepository = productRepository;
        this.processorRepository = processorRepository;
        this.calculationService = calculationService;
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
        // FIX: Use the quantity from the request!
        item.setQuantity(cartRequest.getQuantity());
        tempCart.setItems(List.of(item));

        String finalPriceDisplay = calculationService.calcularTotal(tempCart);

        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("processors", processorRepository.findAll());
        model.addAttribute("selectedProduct", product);
        model.addAttribute("selectedProcessor", processor);
        model.addAttribute("selectedQuantity", cartRequest.getQuantity()); // Added for display
        model.addAttribute("finalPrice", finalPriceDisplay);

        return "cart-view";
    }
}