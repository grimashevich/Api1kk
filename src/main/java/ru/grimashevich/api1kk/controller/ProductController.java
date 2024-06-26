package ru.grimashevich.api1kk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.grimashevich.api1kk.entity.Product;
import ru.grimashevich.api1kk.service.ProductService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PostMapping("/create_all")
    public ResponseEntity<List<Product>> createAllProducts(@RequestBody List<Product> products) {
        List<Product> savedProducts = productService.saveAll(products);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducts);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Product> get(@PathVariable long id) {
        Product product = productService.findById(id).orElse(null);
        if (Objects.isNull(product)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }
}
