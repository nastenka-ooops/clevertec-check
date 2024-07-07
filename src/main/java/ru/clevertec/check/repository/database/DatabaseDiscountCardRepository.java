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
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, number);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
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

    @Override
    public Optional<DiscountCard> findById(int id) {
        String query = "SELECT * FROM discount_card WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
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

    @Override
    public void createDiscountCard(DiscountCard discountCard) {
        String query = "INSERT INTO discount_card (number, amount) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, discountCard.getDiscountCard());
            preparedStatement.setInt(2, discountCard.getDiscountAmount());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR");
        }
    }

    @Override
    public boolean updateDiscountCardById(int id, DiscountCard discountCard) {
        String query = "UPDATE discount_card SET number = ?, amount = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, discountCard.getDiscountCard());
            preparedStatement.setInt(2, discountCard.getDiscountAmount());
            preparedStatement.setInt(3, discountCard.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR");
        }
    }

    @Override
    public boolean deleteDiscountCardById(int id) {
        String sql = "DELETE FROM discount_card WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR");
        }
    }
}
