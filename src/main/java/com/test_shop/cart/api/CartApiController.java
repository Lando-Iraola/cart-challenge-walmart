package com.test_shop.cart.api;

import com.test_shop.cart.dto.CalculationResult;
import com.test_shop.cart.dto.CartRequest;
import com.test_shop.cart.model.Cart;
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

import java.util.stream.Collectors;

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
     * Receives a list of products and quantities to calculate the final price breakdown.
     */
    @PostMapping("/calculate")
    public ResponseEntity<CalculationResult> calculate(@RequestBody CartRequest cartRequest) {
        // 1. Resolve the Payment Processor
        PaymentProcessor processor = processorRepository.findById(cartRequest.getProcessorId())
                .orElseThrow(() -> new RuntimeException("Payment Processor not found: " + cartRequest.getProcessorId()));

        // 2. Setup the transient Cart object
        Cart tempCart = new Cart();
        tempCart.setPaymentProcessor(processor);
        tempCart.setTaxRate("0.19"); // Standard 19% tax rate

        // 3. Process the list of items from the DTO
        if (cartRequest.getItems() == null || cartRequest.getItems().isEmpty()) {
            throw new IllegalArgumentException("The cart must contain at least one item.");
        }

        for (CartRequest.ItemRequest itemReq : cartRequest.getItems()) {
            // Fetch product for each item in the request
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));
            
            // Add to cart using your model's logic
            tempCart.addItem(product, itemReq.getQuantity());
        }

        // 4. Calculate total using the Rule Engine via Service
        CalculationResult result = calculationService.calcularTotal(tempCart);

        // 5. Return the full breakdown DTO as JSON
        return ResponseEntity.ok(result);
    }
}