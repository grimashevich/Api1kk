package ru.grimashevich.api1kk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.grimashevich.api1kk.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
