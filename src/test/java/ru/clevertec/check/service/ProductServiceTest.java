package ru.clevertec.check.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.repository.interfaces.ProductRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void getProductById_ProductExist(){
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1);

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void getProductById_ProductNotExist(){
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(1);

        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findById(1);
    }
}
