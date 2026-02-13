package com.test_shop.cart.api;

import com.test_shop.cart.dto.CalculationResult;
import com.test_shop.cart.dto.CartRequest;
import com.test_shop.cart.model.Cart;
import com.test_shop.cart.model.CartItem;
import com.test_shop.cart.model.PaymentProcessor;
import com.test_shop.cart.model.Product;
import com.test_shop.cart.repository.PaymentProcessorRepository;
import com.test_shop.cart.repository.ProductRepository;
import com.test_shop.cart.service.CartCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartApiController {

    private final ProductRepository productRepository;
    private final PaymentProcessorRepository processorRepository;
    private final CartCalculationService calculationService;

    public CartApiController(ProductRepository productRepository,
                             PaymentProcessorRepository processorRepository,
                             CartCalculationService calculationService) {
        this.productRepository = productRepository;
        this.processorRepository = processorRepository;
        this.calculationService = calculationService;
    }

    /**
     * POST /api/cart/calculate
     * This endpoint mirrors the simulator logic but returns pure JSON.
     * Perfect for a mobile app or a modern JS frontend.
     */
    @PostMapping("/calculate")
    public ResponseEntity<CalculationResult> calculate(@RequestBody CartRequest cartRequest) {
        // 1. Resolve entities from the IDs provided in the JSON body
        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        PaymentProcessor processor = processorRepository.findById(cartRequest.getProcessorId())
                .orElseThrow(() -> new RuntimeException("Payment Processor not found"));

        // 2. Setup the transient Cart object
        Cart tempCart = new Cart();
        tempCart.setPaymentProcessor(processor);
        tempCart.setTaxRate("0.19");

        // 3. Add the item (Uses the logic from your Cart model)
        tempCart.addItem(product, cartRequest.getQuantity());

        // 4. Calculate
        CalculationResult result = calculationService.calcularTotal(tempCart);

        // 5. Return the full breakdown DTO as JSON
        return ResponseEntity.ok(result);
    }
}