package ru.grimashevich.api1kk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.grimashevich.api1kk.entity.Product;
import ru.grimashevich.api1kk.repository.ProductRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ProductService {
    private Optional<Product> returnProduct = Optional.of(new Product(1L, 2));

    ConcurrentHashMap<Long, Optional<Product>> productCache = new ConcurrentHashMap<>(100000);

    private final ProductRepository productRepository;

    public Product save (Product product) {
        return productRepository.save(product);
    }

    public List<Product> saveAll(List<Product> products) {
        return productRepository.saveAll(products);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public Optional<Product> findById(Long id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }
        Optional<Product> product = productCache.get(id);
        if (Objects.isNull(product)) {
            product = productRepository.findById(id);
            productCache.put(id, product);
        }
        return product;
    }
}
