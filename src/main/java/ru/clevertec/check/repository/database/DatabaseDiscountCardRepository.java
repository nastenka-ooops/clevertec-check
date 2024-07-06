package ru.clevertec.check.repository.database;

import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.repository.interfaces.DiscountCardRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseDiscountCardRepository implements DiscountCardRepository {
    private final Connection connection;

    public DatabaseDiscountCardRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<DiscountCard> findAll() {
        List<DiscountCard> discountCards = new ArrayList<>();
        String query = "SELECT * FROM discount_card";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                DiscountCard discountCard = new DiscountCard(
                        resultSet.getInt("id"),
                        resultSet.getInt("number"),
                        resultSet.getInt("amount")
                );
                discountCards.add(discountCard);
            }
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR");
        }
        return discountCards;
    }

    @Override
    public Optional<DiscountCard> findByNumber(int number) {
        String query = "SELECT * FROM discount_card WHERE number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, number);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    DiscountCard discountCard = new DiscountCard(
                            resultSet.getInt("id"),
                            resultSet.getInt("number"),
                            resultSet.getInt("amount")
                    );
                    return Optional.of(discountCard);
                }
            }
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR");
        }
        return Optional.empty();
    }
}
