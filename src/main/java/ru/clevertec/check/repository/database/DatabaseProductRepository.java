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
            throw new InternalServerException("INTERNAL SERVER ERROR");
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
            throw new InternalServerException("INTERNAL SERVER ERROR");
        }
        return Optional.empty();
    }
}
