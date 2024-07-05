package main.java.ru.clevertec.check.util;

import main.java.ru.clevertec.check.entity.Check;
import main.java.ru.clevertec.check.entity.CheckItem;
import main.java.ru.clevertec.check.entity.DiscountCard;
import main.java.ru.clevertec.check.entity.Product;
import main.java.ru.clevertec.check.exception.InternalServerException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvUtil {
    public static List<Product> readProducts(String fileName) {
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            return lines.skip(1).map(line -> {
                String[] values = line.split(";");
                return new Product(Integer.parseInt(values[0]), values[1], Double.parseDouble(values[2]),
                        Integer.parseInt(values[3]), Boolean.parseBoolean(values[4]));
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new InternalServerException("Error reading products from file");
        }
    }

    public static List<DiscountCard> readDiscountCards(String fileName) {
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            return lines.skip(1).map(line -> {
                String[] values = line.split(";");
                return new DiscountCard(Integer.parseInt(values[0]), values[1], Integer.parseInt(values[2]));
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new InternalServerException("Error reading discount cards from file");
        }
    }

    public static void saveCheck(String filePath, Check check, DiscountCard discountCard) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        try (FileWriter writer = new FileWriter(filePath)) {
            LocalDateTime now = LocalDateTime.now();
            writer.append("Date;Time\n");
            writer.append(dateFormatter.format(now)).append(";")
                    .append(timeFormatter.format(now)).append(";\n");
            writer.append("\nQTY;DESCRIPTION;PRICE;TOTAL;DISCOUNT\n");
            for (CheckItem item : check.getCheckItems()) {
                writer.append(String.valueOf(item.getQuantity())).append(";")
                        .append(item.getProduct().getDescription()).append(";")
                        .append(String.format(Locale.US, "%.2f", item.getProduct().getPrice())).append("$;")
                        .append(String.format(Locale.US, "%.2f", item.getTotalPrice())).append("$;")
                        .append(String.format(Locale.US, "%.2f", item.getDiscount())).append("$;").append("\n");
            }

            if (discountCard != null) {
                writer.append("\nDISCOUNT CARD;DISCOUNT PERCENTAGE\n");
                writer.append(discountCard.getCardNumber()).append(";")
                        .append(String.format(Locale.US, "%d", discountCard.getDiscountAmount()))
                        .append("%;").append("\n");

            }
            writer.append("\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n");
            writer.append(String.format(Locale.US, "%.2f", check.getTotalPrice())).append("$;")
                    .append(String.format(Locale.US, "%.2f", check.getTotalDiscount())).append("$;")
                    .append(String.format(Locale.US, "%.2f", check.getTotalPriceWithDiscount())).append("$\n");
        } catch (IOException e) {
            throw new InternalServerException("Error saving result file");
        }
    }

    public static void saveError(String filePath, String errorMessage) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ERROR\n");
            writer.append(errorMessage).append("\n");
        } catch (IOException e) {
            throw new InternalServerException("Error saving result file");
        }
    }

}
