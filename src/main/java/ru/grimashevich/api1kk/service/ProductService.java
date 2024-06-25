package ru.grimashevich.api1kk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.grimashevich.api1kk.entity.Product;
import ru.grimashevich.api1kk.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

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
        return productRepository.findById(id);
    }
}
