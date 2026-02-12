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
        // 1. Fetch the real entities from DB
        Product product = productRepository.findById(cartRequest.getProductId()).orElseThrow();
        PaymentProcessor processor = processorRepository.findById(cartRequest.getProcessorId()).orElseThrow();

        // 2. Create a temporary Cart and CartItem to satisfy your Service logic
        Cart tempCart = new Cart();
        tempCart.setPaymentProcessor(processor);
        tempCart.setTaxRate("0.19"); // Example tax rate (19%)

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(1); // For now, we assume 1 item
        tempCart.setItems(List.of(item));

        // 3. Call your specific method name: 'calcularTotal'
        // Note: your service returns a String (total.getValueWithTax())
        String finalPriceDisplay = calculationService.calcularTotal(tempCart);

        // 4. Pass everything to the view
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("processors", processorRepository.findAll());
        model.addAttribute("selectedProduct", product);
        model.addAttribute("selectedProcessor", processor);
        model.addAttribute("finalPrice", finalPriceDisplay);

        return "cart-view";
    }
}