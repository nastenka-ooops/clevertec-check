/*
package ru.clevertec.check.controller;

import ru.clevertec.check.config.DatabaseConfig;
import ru.clevertec.check.entity.Check;
import ru.clevertec.check.entity.CheckItem;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.repository.database.DatabaseDiscountCardRepository;
import ru.clevertec.check.repository.database.DatabaseProductRepository;
import ru.clevertec.check.repository.interfaces.DiscountCardRepository;
import ru.clevertec.check.repository.interfaces.ProductRepository;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.util.CsvUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class CheckController {
    public static final String PRODUCTS_FILE_PATH = "./src/main/resources/products.csv";
    public static final String DISCOUNT_CARDS_FILE_PATH = "./src/main/resources/discountCards.csv";
    public static final String RESULTS_FILE_PATH = "result.csv";
    public static final String DISCOUNT_CARD_PATTERN = "discountCard";
    public static final String BALANCED_DEBIT_CARD_PATTERN = "balanceDebitCard";
    public static final String SAVE_TO_FILE_PATTERN = "saveToFile";
    public static final String DB_URL_PATTERN = "datasource.url";
    public static final String DB_USERNAME_PATTERN = "datasource.username";
    public static final String DB_PASSWORD_PATTERN = "datasource.password";
    public static final String EQUALS_REGEXP = "=";
    public static final String DASH_REGEXP = "-";
    public static final String ITEM_REGEXP = "\\d+-\\d+";
    public static final int DEFAULT_DISCOUNT_RATE = 2;

    public void create(String[] args) throws ClassNotFoundException {
        String saveToFile = null;
        String dbUrl = null;
        String dbUsername = null;
        String dbPassword = null;

        if (args.length < 2) {
            throw new BadRequestException("BAD REQUEST");
        }

        Map<Integer, Integer> productQuantities = new HashMap<>();
        int discountCardNumber = 0;
        double balanceDebitCard = 0;

        for (String arg : args) {
            if (arg.startsWith(DISCOUNT_CARD_PATTERN)) {
                discountCardNumber = Integer.parseInt(arg.split(EQUALS_REGEXP)[1]);
            } else if (arg.startsWith(BALANCED_DEBIT_CARD_PATTERN)) {
                balanceDebitCard = Double.parseDouble(arg.split(EQUALS_REGEXP)[1]);
            } else if (arg.startsWith(SAVE_TO_FILE_PATTERN)) {
                saveToFile = arg.split(EQUALS_REGEXP)[1];
            } else if (arg.startsWith(DB_URL_PATTERN)) {
                dbUrl = arg.split(EQUALS_REGEXP)[1];
            } else if (arg.startsWith(DB_USERNAME_PATTERN)) {
                dbUsername = arg.split(EQUALS_REGEXP)[1];
            } else if (arg.startsWith(DB_PASSWORD_PATTERN)) {
                dbPassword = arg.split(EQUALS_REGEXP)[1];
            } else if (arg.matches(ITEM_REGEXP)) {
                String[] parts = arg.split(DASH_REGEXP);
                int productId = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);
                productQuantities.merge(productId, quantity, Integer::sum);
            }
        }

        if (dbUrl == null || dbUsername == null || dbPassword == null) {
            throw new BadRequestException("BAD REQUEST");
        }

        if (saveToFile == null) {
            throw new BadRequestException("BAD REQUEST");
        }

        if (balanceDebitCard <= 0) {
            throw new NotEnoughMoneyException("NOT ENOUGH MONEY");
        }

        DatabaseConfig databaseConfig = new DatabaseConfig();

        try (Connection connection = databaseConfig.getConnection()) {

            ProductRepository productRepository = new DatabaseProductRepository(connection);
            DiscountCardRepository discountCardRepository = new DatabaseDiscountCardRepository(connection);

            ProductService productService = new ProductService(productRepository);
            DiscountCardService discountCardService = new DiscountCardService(discountCardRepository);
            CheckService checkService = new CheckService(productService, discountCardService);

            DiscountCard discountCard = getDiscountCard(discountCardService, discountCardNumber);

            Check check = checkService.createCheck(productQuantities, discountCard, balanceDebitCard);
            CsvUtil.saveCheck(saveToFile, check, discountCard);

            printCheck(check, discountCard);
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER EXCEPTION", e);
        }
    }

    public DiscountCard getDiscountCard(DiscountCardService discountCardService, int discountCardNumber) {
        DiscountCard discountCard = null;

        if (discountCardNumber > 0) {
            Optional<DiscountCard> discountCardOpt = discountCardService.getDiscountCardByNumber(discountCardNumber);
            discountCard = discountCardOpt.orElseGet(() -> new DiscountCard((int) (Math.random() * 100),
                    discountCardNumber, DEFAULT_DISCOUNT_RATE));
        }
        return discountCard;
    }

    public void printCheck(Check check, DiscountCard discountCard) {
        System.out.println("Product ID, Product Description, Quantity, Price, Total, Discount");
        for (CheckItem item : check.getCheckItems()) {
            Product product = item.getProduct();
            System.out.printf(Locale.US, "%d, %s, %d, %.2f, %.2f, %.2f\n", product.getId(), product.getDescription(), item.getQuantity(),
                    product.getPrice(), item.getTotalPrice(), item.getDiscount());
        }

        if (discountCard != null) {
            System.out.printf("Discount card %s, Discount percentage %d%%\n", discountCard.getDiscountCard(),
                    discountCard.getDiscountAmount());
        }
        System.out.printf(Locale.US, "Total: %.2f\n", check.getTotalPrice());
        System.out.printf(Locale.US, "Total Discount: %.2f\n", check.getTotalDiscount());
        System.out.printf(Locale.US, "Final Total: %.2f\n", check.getTotalPriceWithDiscount());

    }
}
*/
