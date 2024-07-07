package ru.clevertec.check.service;

import ru.clevertec.check.dto.CheckRequest;
import ru.clevertec.check.dto.ProductRequest;
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
    private final DiscountCardService discountCardService;

    public CheckService(ProductService productService, DiscountCardService discountCardService) {
        this.productService = productService;
        this.discountCardService = discountCardService;
    }

    public Check createCheck(CheckRequest checkRequest) {
        List<CheckItem> checkItems = new ArrayList<>();
        DiscountCard discountCard = getDiscountCard(checkRequest.getDiscountCard());

        int cardDiscountRate = getCardDiscountRate(discountCard);
        double totalPrice = 0;
        double totalDiscount = 0;

        validateProductList(checkRequest.getProducts());

        for (ProductRequest productRequest : checkRequest.getProducts()) {
            Product product = getProduct(productRequest.getId());
            int quantity = validateQuantity(productRequest.getQuantity(), product.getQuantity());

            product.setQuantity(product.getQuantity() - quantity);
            productService.updateProductById(product.getId(), product);

            double productPrice = calculateProductPrice(product, quantity);
            double productDiscount = calculateProductDiscount(product, quantity, cardDiscountRate);

            CheckItem checkItem = new CheckItem(product, quantity, productPrice, productDiscount);
            checkItems.add(checkItem);

            totalPrice += productPrice;
            totalDiscount += productDiscount;
        }

        double finalTotal = calculateFinalTotal(totalPrice, totalDiscount);
        validateBalance(finalTotal, checkRequest.getBalanceDebitCard());

        return new Check(new Date(), new Time(System.currentTimeMillis()), checkItems, totalPrice,
                totalDiscount, finalTotal);
    }

    public Check createCheck(Map<Integer, Integer> products, DiscountCard discountCard, double balanceDebitCard) {
        List<CheckItem> checkItems = new ArrayList<>();
        int cardDiscountRate = getCardDiscountRate(discountCard);
        double totalPrice = 0;
        double totalDiscount = 0;

        validateProductMap(products);

        for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
            Product product = getProduct(entry.getKey());
            int quantity = validateQuantity(entry.getValue(), product.getQuantity());

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

    public DiscountCard getDiscountCard(int discountCardNumber) {
        if (discountCardNumber <= 0) {
            return null;
        }
        Optional<DiscountCard> discountCardOptional = discountCardService.getDiscountCardByNumber(discountCardNumber);
        if (discountCardOptional.isEmpty()) {
            DiscountCard createdDiscountCard = new DiscountCard(null, discountCardNumber, 2);
            discountCardService.createDiscountCard(createdDiscountCard);
            return createdDiscountCard;
        } else {
            return discountCardOptional.get();
        }
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
        if (product.getIsWholesale() && quantity >= 5) {
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

    private void validateProductMap(Map<Integer, Integer> products) {
        if (products.isEmpty()) {
            throw new BadRequestException("BAD REQUEST");
        }
    }

    private void validateProductList(List<ProductRequest> products) {
        if (products.isEmpty()) {
            throw new BadRequestException("BAD REQUEST");
        }
    }
}
