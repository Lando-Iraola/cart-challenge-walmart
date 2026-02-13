package com.test_shop.cart.api;

import com.test_shop.cart.model.PaymentProcessor;
import com.test_shop.cart.repository.PaymentProcessorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/processors")
public class PaymentProcessorApiController {

    private final PaymentProcessorRepository processorRepository;

    public PaymentProcessorApiController(PaymentProcessorRepository processorRepository) {
        this.processorRepository = processorRepository;
    }

    /**
     * GET /api/processors
     * Fetch all available payment methods (Masterplop, PayPal, etc.)
     */
    @GetMapping
    public List<PaymentProcessor> getAllProcessors() {
        return processorRepository.findAll();
    }

    /**
     * GET /api/processors/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentProcessor> getProcessorById(@PathVariable UUID id) {
        return processorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/processors
     * Create a new processor. Uses 409 Conflict if the name exists.
     */
    @PostMapping
    public ResponseEntity<?> createProcessor(@RequestBody PaymentProcessor processor) {
        // Checking for name uniqueness to match your Model constraints
        if (processorRepository.findByName(processor.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Processor '" + processor.getName() + "' already exists.");
        }

        PaymentProcessor saved = processorRepository.save(processor);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * DELETE /api/processors/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcessor(@PathVariable UUID id) {
        if (!processorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        processorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}