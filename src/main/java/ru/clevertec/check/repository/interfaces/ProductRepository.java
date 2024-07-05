package main.java.ru.clevertec.check.repository.interfaces;

import main.java.ru.clevertec.check.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();

    Optional<Product> findById(int id);
}
