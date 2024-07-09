package ru.clevertec.check.util;

import org.junit.jupiter.api.Test;
import ru.clevertec.check.entity.Check;
import ru.clevertec.check.entity.CheckItem;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.entity.Product;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvUtilTest {

    @Test
    void saveCheck_WithDiscountCard() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        CheckItem checkItem = new CheckItem(product, 5, 5.35, 0.53);
        List<CheckItem> checkItems = new ArrayList<>();
        checkItems.add(checkItem);
        Check check = new Check(new Date(System.currentTimeMillis()), new java.sql.Time(System.currentTimeMillis()), checkItems, 5.35, 0.53, 4.82);
        DiscountCard discountCard = new DiscountCard(1, 1111, 3);

        List<String> result = CsvUtil.saveCheck(check, discountCard);

        assertTrue(result.contains("Date;Time"));
        assertTrue(result.contains("\nQTY;DESCRIPTION;PRICE;TOTAL;DISCOUNT"));
        assertTrue(result.contains("5;Milk;1.07$;5.35$;0.53$;"));
        assertTrue(result.contains("\nDISCOUNT CARD;DISCOUNT PERCENTAGE"));
        assertTrue(result.contains("1111;3%;"));
        assertTrue(result.contains("\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT"));
        assertTrue(result.contains("5.35$;0.53$;4.82$"));
    }

    @Test
    void saveCheck_WithoutDiscountCard() {
        Product product = new Product(1, "Milk", 1.07, 10, true);
        CheckItem checkItem = new CheckItem(product, 5, 5.35, 0.53);
        List<CheckItem> checkItems = new ArrayList<>();
        checkItems.add(checkItem);
        Check check = new Check(new Date(System.currentTimeMillis()), new java.sql.Time(System.currentTimeMillis()), checkItems, 5.35, 0.53, 4.82);

        List<String> result = CsvUtil.saveCheck(check, null);

        assertTrue(result.contains("Date;Time"));
        assertTrue(result.contains("\nQTY;DESCRIPTION;PRICE;TOTAL;DISCOUNT"));
        assertTrue(result.contains("5;Milk;1.07$;5.35$;0.53$;"));
        assertTrue(result.contains("\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT"));
        assertTrue(result.contains("5.35$;0.53$;4.82$"));
    }

    @Test
    void saveError() {
        String errorMessage = "An error occurred";

        List<String> result = CsvUtil.saveError(errorMessage);

        assertEquals(2, result.size());
        assertEquals("ERROR", result.get(0));
        assertEquals(errorMessage, result.get(1));
    }
}
