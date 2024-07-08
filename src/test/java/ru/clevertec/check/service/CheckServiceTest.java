package ru.clevertec.check.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.dto.CheckRequest;
import ru.clevertec.check.dto.ProductRequest;
import ru.clevertec.check.entity.Check;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.NotEnoughMoneyException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckServiceTest {
    private ProductService productService;
    private DiscountCardService discountCardService;
    private CheckService checkService;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        discountCardService = mock(DiscountCardService.class);
        checkService = new CheckService(productService, discountCardService);
    }

    @Test
    void createCheck_ValidData() {
        Product product1 = new Product(1, "Milk", 1.07, 10, true);
        Product product2 = new Product(2, "Cream", 2.71, 20, true);
        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        when(productService.getProductById(1)).thenReturn(Optional.of(product1));
        when(productService.getProductById(2)).thenReturn(Optional.of(product2));
        when(discountCardService.getDiscountCardById(1)).thenReturn(Optional.of(discountCard));

        CheckRequest checkRequest = new CheckRequest(List.of(
                new ProductRequest(product1.getId(), 5),
                new ProductRequest(product2.getId(), 3)
        ), 1111, 100.0);

        Check check = checkService.createCheck(checkRequest);

        assertNotNull(check);
        assertEquals(2, check.getCheckItems().size());
        assertEquals(12.78, check.getTotalPriceWithDiscount(), 0.01);
    }

    @Test
    void createCheck_ProductNotFound() {
        when(productService.getProductById(anyInt())).thenReturn(Optional.empty());

        CheckRequest checkRequest = new CheckRequest(List.of(
                new ProductRequest(0, 5)
        ), 1111, 100.0);

        assertThrows(BadRequestException.class, () -> {
            checkService.createCheck(checkRequest);
        });
    }

    @Test
    void createCheck_NotEnoughMoney() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        CheckRequest checkRequest = new CheckRequest(List.of(
                new ProductRequest(product.getId(), 5)
        ), 1111, 3.0);


        assertThrows(NotEnoughMoneyException.class, () -> {
            checkService.createCheck(checkRequest);
        });
    }

    @Test
    void createCheck_ZeroQuantity() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        CheckRequest checkRequest = new CheckRequest(List.of(
                new ProductRequest(product.getId(), 0)
        ), 1111, 100.0);


        assertThrows(BadRequestException.class, () -> {
            checkService.createCheck(checkRequest);
        });
    }

    @Test
    void createCheck_NegativeQuantity() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        CheckRequest checkRequest = new CheckRequest(List.of(
                new ProductRequest(product.getId(), -10)
        ), 1111, 100.0);


        assertThrows(BadRequestException.class, () -> {
            checkService.createCheck(checkRequest);
        });
    }

    @Test
    void createCheck_InvalidQuantity() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        CheckRequest checkRequest = new CheckRequest(List.of(
                new ProductRequest(product.getId(), 30)
        ), 1111, 100.0);


        assertThrows(BadRequestException.class, () -> {
            checkService.createCheck(checkRequest);
        });
    }

    @Test
    void createCheck_DiscountCardApplied() {
        Product product = new Product(1, "Milk", 1.07, 10, false);
        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));
        when(discountCardService.getDiscountCardById(1)).thenReturn(Optional.of(discountCard));

        CheckRequest checkRequest = new CheckRequest(List.of(
                new ProductRequest(product.getId(), 5)
        ), 1111, 100.0);


        Check check = checkService.createCheck(checkRequest);

        assertNotNull(check);
        assertEquals(1, check.getCheckItems().size());
        assertEquals(0.10, check.getCheckItems().getFirst().getDiscount(), 0.01);
    }


    @Test
    void createCheck_NoDiscountCard() {
        Product product = new Product(1, "Milk", 1.07, 10, false);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        CheckRequest checkRequest = new CheckRequest(List.of(
                new ProductRequest(product.getId(), 3)
        ), 0, 100.0);

        Check check = checkService.createCheck(checkRequest);

        assertNotNull(check);
        assertEquals(1, check.getCheckItems().size());
        assertEquals(0, check.getCheckItems().getFirst().getDiscount(), 0.01);
    }

    @Test
    void createCheck_WholesaleDiscountCardApplied() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        CheckRequest checkRequest = new CheckRequest(List.of(
                new ProductRequest(product.getId(), 5)
        ), 0, 100.0);


        Check check = checkService.createCheck(checkRequest);

        assertNotNull(check);
        assertEquals(1, check.getCheckItems().size());
        assertEquals(0.53, check.getCheckItems().getFirst().getDiscount(), 0.01);
    }

    @Test
    void createCheck_EmptyProductList() {
        CheckRequest checkRequest = new CheckRequest(List.of(), 1111, 100.0);

        assertThrows(BadRequestException.class, () -> {
            checkService.createCheck(checkRequest);
        });
    }

    @Test
    void createCheck_ZeroBalance() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        CheckRequest checkRequest = new CheckRequest(List.of(
                new ProductRequest(product.getId(), 5)
        ), 0, 0);


        assertThrows(NotEnoughMoneyException.class, () -> {
            checkService.createCheck(checkRequest);
        });
    }

    @Test
    void createCheck_NegativeBalance() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        CheckRequest checkRequest = new CheckRequest(List.of(
                new ProductRequest(product.getId(), 5)
        ), 0, -10);

        assertThrows(NotEnoughMoneyException.class, () -> {
            checkService.createCheck(checkRequest);
        });
    }
}