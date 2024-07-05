package main.java.ru.clevertec.check.controller;

import main.java.ru.clevertec.check.entity.Check;
import main.java.ru.clevertec.check.entity.CheckItem;
import main.java.ru.clevertec.check.entity.DiscountCard;
import main.java.ru.clevertec.check.entity.Product;
import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.exception.NotEnoughMoneyException;
import main.java.ru.clevertec.check.repository.InMemoryDiscountCardRepository;
import main.java.ru.clevertec.check.repository.InMemoryProductRepository;
import main.java.ru.clevertec.check.repository.interfaces.DiscountCardRepository;
import main.java.ru.clevertec.check.repository.interfaces.ProductRepository;
import main.java.ru.clevertec.check.service.CheckService;
import main.java.ru.clevertec.check.service.DiscountCardService;
import main.java.ru.clevertec.check.service.ProductService;
import main.java.ru.clevertec.check.util.CsvUtil;

import java.util.*;

public class CheckController {
    public static final String PRODUCTS_FILE_PATH = "./src/main/resources/products.csv";
    public static final String DISCOUNT_CARDS_FILE_PATH = "./src/main/resources/discountCards.csv";
    public static final String RESULTS_FILE_PATH = "result.csv";
    public static final int DEFAULT_DISCOUNT_RATE = 2;

    List<Product> products = CsvUtil.readProducts(PRODUCTS_FILE_PATH);
    List<DiscountCard> discountCards = CsvUtil.readDiscountCards(DISCOUNT_CARDS_FILE_PATH);

    ProductRepository productRepository = new InMemoryProductRepository(products);
    ProductService productService = new ProductService(productRepository);
    DiscountCardRepository discountCardRepository = new InMemoryDiscountCardRepository(discountCards);
    DiscountCardService discountCardService = new DiscountCardService(discountCardRepository);
    CheckService checkService = new CheckService(productService);

    public void create(String[] args) {
        if (args.length < 2) {
            throw new BadRequestException("BAD REQUEST");
        }

        Map<Integer, Integer> productQuantities = new HashMap<>();
        String discountCardNumber = null;
        double balanceDebitCard = 0;

        for (String arg : args) {
            if (arg.startsWith("discountCard=")) {
                discountCardNumber = arg.split("=")[1];
            } else if (arg.startsWith("balanceDebitCard=")) {
                balanceDebitCard = Double.parseDouble(arg.split("=")[1]);
            } else if (arg.matches("\\d+-\\d+")) {
                String[] parts = arg.split("-");
                int productId = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);
                productQuantities.merge(productId, quantity, Integer::sum);
            }
        }

        if (productQuantities.isEmpty()) {
            throw new BadRequestException("BAD REQUEST");
        }

        if (balanceDebitCard <= 0) {
            throw new NotEnoughMoneyException("NOT ENOUGH MONEY");
        }

        DiscountCard discountCard = null;

        if (discountCardNumber != null && !discountCardNumber.isEmpty()) {
            Optional<DiscountCard> discountCardOpt = discountCardService.getDiscountCardByNumber(discountCardNumber);
            if (discountCardOpt.isEmpty()) {
                discountCard = new DiscountCard((int) (Math.random() * 100), discountCardNumber, DEFAULT_DISCOUNT_RATE);
            } else {
                discountCard = discountCardOpt.get();
            }
        }

        Check check = checkService.createCheck(productQuantities, discountCard, balanceDebitCard);
        CsvUtil.saveCheck(RESULTS_FILE_PATH, check, discountCard);

        printCheck(check, discountCard);
    }

    public void printCheck(Check check, DiscountCard discountCard) {
        System.out.println("Product ID, Product Description, Quantity, Price, Total, Discount");
        for (CheckItem item : check.getCheckItems()) {
            Product product = item.getProduct();
            System.out.printf(Locale.US, "%d, %s, %d, %.2f, %.2f, %.2f\n", product.getId(), product.getDescription(), item.getQuantity(),
                    product.getPrice(), item.getTotalPrice(), item.getDiscount());
        }

        if (discountCard != null) {
            System.out.printf("Discount card %s, Discount percentage %d%%\n", discountCard.getCardNumber(),
                    discountCard.getDiscountAmount());
        }
        System.out.printf(Locale.US, "Total: %.2f\n", check.getTotalPrice());
        System.out.printf(Locale.US, "Total Discount: %.2f\n", check.getTotalDiscount());
        System.out.printf(Locale.US, "Final Total: %.2f\n", check.getTotalPriceWithDiscount());

    }
}
