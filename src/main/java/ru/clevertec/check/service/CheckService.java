package ru.clevertec.check.service;

import ru.clevertec.check.entity.Check;
import ru.clevertec.check.entity.CheckItem;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.NotEnoughMoneyException;

import java.sql.Time;
import java.util.*;

public class CheckService {

    private final ProductService productService;

    public CheckService(ProductService productService) {
        this.productService = productService;
    }

    public Check createCheck(Map<Integer, Integer> products, DiscountCard discountCard, double balanceDebitCard) {
        List<CheckItem> checkItems = new ArrayList<>();
        int cardDiscountRate = getCardDiscountRate(discountCard);
        double totalPrice = 0;
        double totalDiscount = 0;

        validateProductList(products);

        for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
            Product product = getProduct(entry.getKey());
            int quantity = validateQuantity(entry.getValue(), product.getQuantityInStock());

            double productPrice = calculateProductPrice(product, quantity);
            double productDiscount = calculateProductDiscount(product, quantity, cardDiscountRate);

            CheckItem checkItem = new CheckItem(product, quantity, productPrice, productDiscount);
            checkItems.add(checkItem);

            totalPrice += productPrice;
            totalDiscount += productDiscount;
        }

        double finalTotal = calculateFinalTotal(totalPrice, totalDiscount);
        validateBalance(finalTotal, balanceDebitCard);

        return new Check(new Date(), new Time(System.currentTimeMillis()), checkItems, totalPrice,
                totalDiscount, finalTotal);
    }

    private int getCardDiscountRate(DiscountCard discountCard) {
        return discountCard != null ? discountCard.getDiscountAmount() : 0;
    }

    private Product getProduct(int productId) {
        Optional<Product> productOptional = productService.getProductById(productId);
        if (productOptional.isEmpty()) {
            throw new BadRequestException("BAD REQUEST");
        }
        return productOptional.get();
    }

    private int validateQuantity(int quantity, int quantityInStock) {
        if (quantity <= 0 || quantity > quantityInStock) {
            throw new BadRequestException("BAD REQUEST");
        }
        return quantity;
    }

    private double calculateProductPrice(Product product, int quantity) {
        return product.getPrice() * quantity;
    }

    private double calculateProductDiscount(Product product, int quantity, int cardDiscountRate) {
        if (product.isWholesaleProduct() && quantity >= 5) {
            return calculateProductPrice(product, quantity) * 0.1;
        } else {
            return calculateProductPrice(product, quantity) * cardDiscountRate / 100;
        }
    }

    private double calculateFinalTotal(double totalPrice, double totalDiscount) {
        return totalPrice - totalDiscount;
    }

    private void validateBalance(double finalTotal, double balanceDebitCard) {
        if (finalTotal > balanceDebitCard) {
            throw new NotEnoughMoneyException("NOT ENOUGH MONEY");
        }
    }

    private void validateProductList(Map<Integer, Integer> products) {
        if (products.isEmpty()) {
            throw new BadRequestException("BAD REQUEST");
        }
    }
}
