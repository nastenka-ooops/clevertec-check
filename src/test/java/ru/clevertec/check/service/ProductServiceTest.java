package ru.clevertec.check.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.repository.interfaces.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    void getAllProducts() {
        Product product1 = new Product(1, "Milk", 1.07, 10, true);
        Product product2 = new Product(2, "Cream", 2.71, 20, true);
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_ProductExist() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1);

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void getProductById_ProductNotExist() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(1);

        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void createProduct() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        doNothing().when(productRepository).createProduct(product);

        productService.createProduct(product);

        verify(productRepository, times(1)).createProduct(product);
    }

    @Test
    void createProduct_AndThenGetProductById() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        doAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(1);
            return null;
        }).when(productRepository).createProduct(product);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        productService.createProduct(product);
        Optional<Product> result = productService.getProductById(1);

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(productRepository, times(1)).createProduct(product);
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void updateProductById_ProductExist() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productRepository.updateProductById(1, product)).thenReturn(true);

        boolean result = productService.updateProductById(1, product);

        assertTrue(result);
        verify(productRepository, times(1)).updateProductById(1, product);
    }

    @Test
    void updateProductById_ProductNotExist() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productRepository.updateProductById(1, product)).thenReturn(false);

        boolean result = productService.updateProductById(1, product);

        assertFalse(result);
        verify(productRepository, times(1)).updateProductById(1, product);
    }

    @Test
    void deleteProductById_ProductExist() {
        when(productRepository.deleteProductById(1)).thenReturn(true);

        boolean result = productService.deleteProductById(1);

        assertTrue(result);
        verify(productRepository, times(1)).deleteProductById(1);
    }

    @Test
    void deleteProductById_ProductNotExist() {
        when(productRepository.deleteProductById(1)).thenReturn(false);

        boolean result = productService.deleteProductById(1);

        assertFalse(result);
        verify(productRepository, times(1)).deleteProductById(1);
    }

}
