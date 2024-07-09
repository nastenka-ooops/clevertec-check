package ru.clevertec.check.repository.database;

import ru.clevertec.check.entity.Product;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.repository.interfaces.ProductRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseProductRepository implements ProductRepository {
    private final Connection connection;

    public DatabaseProductRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity_in_stock"),
                        resultSet.getBoolean("wholesale_product")
                        );
                products.add(product);
            }
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR", e);
        }
        return products;
    }

    @Override
    public Optional<Product> findById(int id) {
        String query = "SELECT * FROM product WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    Product product = new Product(
                            resultSet.getInt("id"),
                            resultSet.getString("description"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("quantity_in_stock"),
                            resultSet.getBoolean("wholesale_product")
                    );
                    return Optional.of(product);
                }
            }
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR", e);
        }
        return Optional.empty();
    }

    @Override
    public void createProduct(Product product) {
        String query = "INSERT INTO product (description, price, quantity_in_stock, wholesale_product) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, product.getDescription());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setBoolean(4, product.getIsWholesale());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR", e);
        }
    }

    @Override
    public boolean updateProductById(int id, Product product) {
        String query = "UPDATE product SET description = ?, price = ?, quantity_in_stock = ?, wholesale_product = ? " +
                "WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, product.getDescription());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setBoolean(4, product.getIsWholesale());
            preparedStatement.setInt(5, product.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR", e);
        }
    }

    @Override
    public boolean deleteProductById(int id) {
        String sql = "DELETE FROM product WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR", e);
        }
    }

    public List<Product> findSortedProducts(String sortBy) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product ORDER BY " + (sortBy != null ? sortBy : "id");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("description"),
                        rs.getDouble("price"), rs.getInt("quantity_in_stock"),
                        rs.getBoolean("wholesale_product")));
            }
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR", e);
        }
        return products;
    }
}
