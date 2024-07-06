package ru.clevertec.check.repository.inMemory;

import ru.clevertec.check.entity.Product;
import ru.clevertec.check.repository.interfaces.ProductRepository;

import java.util.List;
import java.util.Optional;

public class InMemoryProductRepository implements ProductRepository {

    private final List<Product> products;

    public InMemoryProductRepository(List<Product> products) {
        this.products = products;
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Optional<Product> findById(int id) {
        return products.stream().filter(product -> product.getId() == id).findFirst();
    }
}
