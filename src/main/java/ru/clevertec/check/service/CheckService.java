package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.entity.Check;
import main.java.ru.clevertec.check.entity.CheckItem;
import main.java.ru.clevertec.check.entity.DiscountCard;
import main.java.ru.clevertec.check.entity.Product;
import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.exception.NotEnoughMoneyException;

import java.sql.Time;
import java.util.*;

public class CheckService {

    private final ProductService productService;

    public CheckService(ProductService productService) {
        this.productService = productService;
    }

    public Check createCheck(Map<Integer, Integer> products, DiscountCard discountCard, double balanceDebitCard) {
        List<CheckItem> checkItems = new ArrayList<>();

        int cardDiscountRate = 0;
        if (discountCard != null) {
            cardDiscountRate = discountCard.getDiscountAmount();
        }
        double totalPrice = 0;
        double totalDiscount = 0;

        for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
            Optional<Product> productOptional = productService.getProductById(entry.getKey());

            if (productOptional.isEmpty()) {
                throw new BadRequestException("BAD REQUEST");
            }

            Product product = productOptional.get();
            int quantity = entry.getValue();
            if (quantity <= 0 || quantity > product.getQuantityInStock()) {
                throw new BadRequestException("BAD REQUEST");
            }

            double productPrice = product.getPrice() * quantity;
            double productDiscount;
            if (product.isWholesaleProduct() && quantity >= 5) {
                productDiscount = productPrice * 0.1;
            } else {
                productDiscount = productPrice * cardDiscountRate / 100;
            }

            CheckItem checkItem = new CheckItem(product, quantity, productPrice, productDiscount);
            checkItems.add(checkItem);

            totalPrice += productPrice;
            totalDiscount += productDiscount;

        }

        double finalTotal = totalPrice - totalDiscount;
        if (finalTotal > balanceDebitCard) {
            throw new NotEnoughMoneyException("NOT ENOUGH MONEY");
        }

        return new Check(new Date(), new Time(System.currentTimeMillis()), checkItems, totalPrice,
                totalDiscount, finalTotal);
    }
}
