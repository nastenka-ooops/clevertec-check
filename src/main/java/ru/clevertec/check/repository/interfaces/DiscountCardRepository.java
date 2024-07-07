package ru.clevertec.check.repository.interfaces;


import ru.clevertec.check.entity.DiscountCard;

import java.util.List;
import java.util.Optional;

public interface DiscountCardRepository {
    List<DiscountCard> findAll();

    Optional<DiscountCard> findByNumber(int number);

    Optional<DiscountCard> findById(int id);

    void createDiscountCard(DiscountCard discountCard);

    boolean updateDiscountCardById(int id, DiscountCard discountCard);

    boolean deleteDiscountCardById(int id);
}
