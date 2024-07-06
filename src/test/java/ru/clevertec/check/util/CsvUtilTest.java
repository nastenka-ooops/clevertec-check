package ru.clevertec.check.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.entity.Check;
import ru.clevertec.check.entity.CheckItem;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.entity.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvUtilTest {
    private static final String TEST_DIR = "test_files/";
    private static final String CHECK_FILE = TEST_DIR + "check.csv";
    private static final String ERROR_FILE = TEST_DIR + "error.csv";

    @BeforeEach
    public void setUp() {
        Path directoryPath = Paths.get(TEST_DIR);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                fail("Failed to create test directory: " + e.getMessage());
            }
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            Files.deleteIfExists(Paths.get(CHECK_FILE));
            Files.deleteIfExists(Paths.get(ERROR_FILE));
        } catch (IOException e) {
            fail("Failed to delete test files: " + e.getMessage());
        }
    }

    @Test
    public void testSaveCheck() throws IOException {
        DiscountCard discountCard = new DiscountCard(1,1111, 3);
        Product product = new Product(1, "Milk", 1.07, 10, true);
        CheckItem item1 = new CheckItem(product, 2, 2.14, 0.06);
        Check check = new Check(new Date(), new Time(System.currentTimeMillis()), new ArrayList<>(List.of(item1)),
                2.14, 0.06, 2.08);

        CsvUtil.saveCheck(CHECK_FILE, check, discountCard);

        assertTrue(Files.exists(Paths.get(CHECK_FILE)));

        List<String> lines = readLinesFromFile(CHECK_FILE);
        assertEquals("Date;Time", lines.get(0).trim());
        assertEquals("QTY;DESCRIPTION;PRICE;TOTAL;DISCOUNT", lines.get(3).trim());
        assertEquals("2;Milk;1.07$;2.14$;0.06$;", lines.get(4).trim());
        assertEquals("DISCOUNT CARD;DISCOUNT PERCENTAGE", lines.get(6).trim());
        assertEquals("1111;3%;", lines.get(7).trim());
        assertEquals("TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT", lines.get(9).trim());
        assertEquals("2.14$;0.06$;2.08$", lines.get(10).trim());
    }

    @Test
    public void testSaveError() throws IOException {
        String errorMessage = "BAD REQUEST";

        CsvUtil.saveError(ERROR_FILE, errorMessage);

        assertTrue(Files.exists(Paths.get(ERROR_FILE)));

        List<String> lines = readLinesFromFile(ERROR_FILE);
        assertEquals("ERROR", lines.get(0).trim());
        assertEquals(errorMessage, lines.get(1).trim());
    }

    private List<String> readLinesFromFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
