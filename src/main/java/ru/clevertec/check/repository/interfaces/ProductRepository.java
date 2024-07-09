package ru.clevertec.check.repository.interfaces;

import ru.clevertec.check.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();

    List<Product> findSortedProducts(String sortBy);

    Optional<Product> findById(int id);

    void createProduct(Product product);

    boolean updateProductById(int id, Product product);

    boolean deleteProductById(int id);

}
