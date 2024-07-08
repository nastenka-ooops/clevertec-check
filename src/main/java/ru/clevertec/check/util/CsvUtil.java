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

    public static List<String> saveError(String errorMessage) {
        List<String> writer = new ArrayList<>();
        writer.add("ERROR");
        writer.add(errorMessage);

        return writer;
    }
}
