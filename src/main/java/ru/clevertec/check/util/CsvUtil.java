package ru.clevertec.check.util;

import ru.clevertec.check.entity.Check;
import ru.clevertec.check.entity.CheckItem;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.exception.InternalServerException;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CsvUtil {
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
                writer.append(String.format("%d", discountCard.getDiscountCard())).append(";")
                        .append(String.format(Locale.US, "%d", discountCard.getDiscountAmount()))
                        .append("%;").append("\n");

            }
            writer.append("\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n");
            writer.append(String.format(Locale.US, "%.2f", check.getTotalPrice())).append("$;")
                    .append(String.format(Locale.US, "%.2f", check.getTotalDiscount())).append("$;")
                    .append(String.format(Locale.US, "%.2f", check.getTotalPriceWithDiscount())).append("$\n");
        } catch (IOException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR");
        }
    }

    public static List<String> saveCheck(Check check, DiscountCard discountCard) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        List<String> writer = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        writer.add("Date;Time");
        writer.add(dateFormatter.format(now) + ";" + timeFormatter.format(now));
        writer.add("\nQTY;DESCRIPTION;PRICE;TOTAL;DISCOUNT");
        for (CheckItem item : check.getCheckItems()) {
            writer.add(item.getQuantity() + ";"
                    + item.getProduct().getDescription() + ";"
                    + String.format(Locale.US, "%.2f", item.getProduct().getPrice()) + "$;"
                    + String.format(Locale.US, "%.2f", item.getTotalPrice()) + "$;"
                    + String.format(Locale.US, "%.2f", item.getDiscount()) + "$;"
                    );
        }

        if (discountCard != null) {
            writer.add("\nDISCOUNT CARD;DISCOUNT PERCENTAGE");
            writer.add(String.format("%d", discountCard.getDiscountCard()) + ";"
                    + String.format(Locale.US, "%d", discountCard.getDiscountAmount()) + "%;"
            );
        }
        writer.add("\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT");
        writer.add(String.format(Locale.US, "%.2f", check.getTotalPrice()) + "$;"
                + String.format(Locale.US, "%.2f", check.getTotalDiscount()) + "$;"
                + String.format(Locale.US, "%.2f", check.getTotalPriceWithDiscount()) + "$"
        );
        return writer;
    }


    public static void saveError(String filePath, String errorMessage) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ERROR\n");
            writer.append(errorMessage).append("\n");
        } catch (IOException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR");
        }
    }

    public static List<String> saveError(String errorMessage) {
        List<String> writer = new ArrayList<>();
        writer.add("ERROR");
        writer.add(errorMessage);

        return writer;
    }
}
