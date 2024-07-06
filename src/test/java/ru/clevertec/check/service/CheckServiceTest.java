package ru.clevertec.check.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.entity.Check;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.NotEnoughMoneyException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckServiceTest {
    private ProductService productService;
    private CheckService checkService;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        checkService = new CheckService(productService);
    }

    @Test
    void createCheck_ValidData() {
        Product product1 = new Product(1, "Milk", 1.07, 10, true);
        Product product2 = new Product(2, "Cream", 2.71, 20, true);
        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        when(productService.getProductById(1)).thenReturn(Optional.of(product1));
        when(productService.getProductById(2)).thenReturn(Optional.of(product2));

        Map<Integer, Integer> products = Map.of(1, 5, 2, 3);

        Check check = checkService.createCheck(products, discountCard, 100);

        assertNotNull(check);
        assertEquals(2, check.getCheckItems().size());
        assertEquals(12.70, check.getTotalPriceWithDiscount(), 0.01);
    }

    @Test
    void createCheck_ProductNotFound() {
        when(productService.getProductById(anyInt())).thenReturn(Optional.empty());

        Map<Integer, Integer> products = Map.of(1, 5);

        assertThrows(BadRequestException.class, () -> {
            checkService.createCheck(products, null, 100);
        });
    }

    @Test
    void createCheck_NotEnoughMoney() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        Map<Integer, Integer> products = Map.of(1, 5);

        assertThrows(NotEnoughMoneyException.class, () -> {
            checkService.createCheck(products, null, 3);
        });
    }

    @Test
    void createCheck_ZeroQuantity() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        Map<Integer, Integer> products = Map.of(1, 0);

        assertThrows(BadRequestException.class, () -> {
            checkService.createCheck(products, null, 100);
        });
    }

    @Test
    void createCheck_NegativeQuantity() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        Map<Integer, Integer> products = Map.of(1, -1);

        assertThrows(BadRequestException.class, () -> {
            checkService.createCheck(products, null, 100);
        });
    }

    @Test
    void createCheck_InvalidQuantity() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        Map<Integer, Integer> products = Map.of(1, 15);

        assertThrows(BadRequestException.class, () -> {
            checkService.createCheck(products, null, 100);
        });
    }

    @Test
    void createCheck_DiscountCardApplied() {
        Product product = new Product(1, "Milk", 1.07, 10, false);
        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        Map<Integer, Integer> products = Map.of(1, 5);

        Check check = checkService.createCheck(products, discountCard, 100);

        assertNotNull(check);
        assertEquals(1, check.getCheckItems().size());
        assertEquals(0.16, check.getCheckItems().getFirst().getDiscount(), 0.01);
    }


    @Test
    void createCheck_NoDiscountCArd(){
        Product product = new Product(1, "Milk", 1.07, 10, false);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        Map<Integer, Integer> products = Map.of(1, 5);

        Check check = checkService.createCheck(products, null, 100);

        assertNotNull(check);
        assertEquals(1, check.getCheckItems().size());
        assertEquals(0, check.getCheckItems().getFirst().getDiscount(), 0.01);
    }

    @Test
    void createCheck_WholesaleDiscountCardApplied() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        Map<Integer, Integer> products = Map.of(1, 5);

        Check check = checkService.createCheck(products, null, 100);

        assertNotNull(check);
        assertEquals(1, check.getCheckItems().size());
        assertEquals(0.53, check.getCheckItems().getFirst().getDiscount(), 0.01);
    }

    @Test
    void createCheck_EmptyProductList() {
        Map<Integer, Integer> products = new HashMap<>();

        assertThrows(BadRequestException.class, () -> {
            checkService.createCheck(products, null, 100);
        });
    }

    @Test
    void createCheck_ZeroBalance() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        Map<Integer, Integer> products = Map.of(1, 5);

        assertThrows(NotEnoughMoneyException.class, () -> {
            checkService.createCheck(products, null, 0);
        });
    }

    @Test
    void createCheck_NegativeBalance() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        Map<Integer, Integer> products = Map.of(1, 5);

        assertThrows(NotEnoughMoneyException.class, () -> {
            checkService.createCheck(products, null, -10);
        });
    }
}
