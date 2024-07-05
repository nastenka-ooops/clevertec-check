package main.java.ru.clevertec.check.repository.interfaces;

import main.java.ru.clevertec.check.entity.DiscountCard;

import java.util.List;
import java.util.Optional;

public interface DiscountCardRepository {
    List<DiscountCard> findAll();

    Optional<DiscountCard> findByNumber(String number);
}
