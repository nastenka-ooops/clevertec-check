package ru.clevertec.check.repository.inMemory;

import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.repository.interfaces.DiscountCardRepository;

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
    public Optional<DiscountCard> findByNumber(int number) {
        return discountCards.stream().filter(card -> card.getCardNumber() == number).findFirst();
    }
}
