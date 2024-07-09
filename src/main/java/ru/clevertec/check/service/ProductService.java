package ru.clevertec.check.service;

import ru.clevertec.check.entity.Product;
import ru.clevertec.check.repository.interfaces.ProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getSortedProducts(String sortBy) {
        return productRepository.findSortedProducts(sortBy);
    }

    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }

    public void createProduct(Product product) {
        productRepository.createProduct(product);
    }

    public boolean updateProductById(int id, Product product) {
        return productRepository.updateProductById(id, product);
    }

    public boolean deleteProductById(int id) {
        return productRepository.deleteProductById(id);
    }

}
