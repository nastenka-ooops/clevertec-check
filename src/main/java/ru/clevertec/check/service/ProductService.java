package ru.clevertec.check.service;

import ru.clevertec.check.entity.Product;
import ru.clevertec.check.repository.interfaces.ProductRepository;

import java.util.Optional;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }
}
