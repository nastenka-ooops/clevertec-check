package main.java.ru.clevertec.check.repository;

import main.java.ru.clevertec.check.entity.DiscountCard;
import main.java.ru.clevertec.check.repository.interfaces.DiscountCardRepository;

import java.util.List;
import java.util.Optional;

public class InMemoryDiscountCardRepository implements DiscountCardRepository {

    private final List<DiscountCard> discountCards;

    public InMemoryDiscountCardRepository(List<DiscountCard> discountCards) {
        this.discountCards = discountCards;
    }

    @Override
    public List<DiscountCard> findAll() {
        return discountCards;
    }

    @Override
    public Optional<DiscountCard> findByNumber(String number) {
        return discountCards.stream().filter(card -> card.getCardNumber().equals(number)).findFirst();
    }
}
