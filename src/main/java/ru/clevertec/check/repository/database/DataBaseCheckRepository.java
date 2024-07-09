package ru.clevertec.check.repository.database;

import ru.clevertec.check.entity.Check;
import ru.clevertec.check.entity.CheckItem;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.repository.interfaces.CheckRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseCheckRepository implements CheckRepository {

    private final Connection connection;

    public DataBaseCheckRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveCheck(Check check) {
        String checkQuery = "INSERT INTO products_check (date, time, total_price, total_discount, total_price_with_discount) VALUES (?, ?, ?, ?, ?)";
        String checkItemQuery = "INSERT INTO check_items (check_id, product_id, quantity, total_price, discount) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            checkStmt.setDate(1, check.getDate());
            checkStmt.setTime(2, check.getTime());
            checkStmt.setDouble(3, check.getTotalPrice());
            checkStmt.setDouble(4, check.getTotalDiscount());
            checkStmt.setDouble(5, check.getTotalPriceWithDiscount());
            checkStmt.executeUpdate();

            var generatedKeys = checkStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int checkId = generatedKeys.getInt(1);
                try (PreparedStatement checkItemStmt = connection.prepareStatement(checkItemQuery)) {
                    for (CheckItem item : check.getCheckItems()) {
                        checkItemStmt.setInt(1, checkId);
                        checkItemStmt.setInt(2, item.getProduct().getId());
                        checkItemStmt.setInt(3, item.getQuantity());
                        checkItemStmt.setDouble(4, item.getTotalPrice());
                        checkItemStmt.setDouble(5, item.getDiscount());
                        checkItemStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR", e);
        }
    }

    @Override
    public List<Check> getAllChecks() {
        String checkSql = "SELECT * FROM products_check";
        String checkItemSql = "SELECT ci.*, p.id as product_id, p.description, p.price, p.quantity_in_stock, p.wholesale_product " +
                "FROM check_items ci JOIN product p ON ci.product_id = p.id WHERE ci.check_id = ?";

        List<Check> checks = new ArrayList<>();
        try (Statement checkStmt = connection.createStatement();
             ResultSet checkRs = checkStmt.executeQuery(checkSql)) {
            while (checkRs.next()) {
                Check check = new Check();
                check.setId(checkRs.getInt("id"));
                check.setDate(checkRs.getDate("date"));
                check.setTime(checkRs.getTime("time"));
                check.setTotalPrice(checkRs.getDouble("total_price"));
                check.setTotalDiscount(checkRs.getDouble("total_discount"));
                check.setTotalPriceWithDiscount(checkRs.getDouble("total_price_with_discount"));

                List<CheckItem> checkItems = new ArrayList<>();
                try (PreparedStatement checkItemStmt = connection.prepareStatement(checkItemSql)) {
                    checkItemStmt.setInt(1, check.getId());
                    try (ResultSet checkItemRs = checkItemStmt.executeQuery()) {
                        while (checkItemRs.next()) {
                            CheckItem item = new CheckItem();
                            Product product = new Product();
                            product.setId(checkItemRs.getInt("product_id"));
                            product.setDescription(checkItemRs.getString("description"));
                            product.setPrice(checkItemRs.getDouble("price"));
                            product.setQuantity(checkItemRs.getInt("quantity_in_stock"));
                            product.setIsWholesale(checkItemRs.getBoolean("wholesale_product"));

                            item.setProduct(product);
                            item.setQuantity(checkItemRs.getInt("quantity"));
                            item.setTotalPrice(checkItemRs.getDouble("total_price"));
                            item.setDiscount(checkItemRs.getDouble("discount"));
                            checkItems.add(item);
                        }
                    }
                }
                check.setCheckItems(checkItems);
                checks.add(check);
            }
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR", e);
        }
        return checks;
    }
}
