package ru.clevertec.check.repository.interfaces;



import ru.clevertec.check.entity.DiscountCard;

import java.util.List;
import java.util.Optional;

public interface DiscountCardRepository {
    List<DiscountCard> findAll();

    Optional<DiscountCard> findByNumber(int number);
}
