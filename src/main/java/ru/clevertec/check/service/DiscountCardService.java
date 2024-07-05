package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.entity.DiscountCard;
import main.java.ru.clevertec.check.repository.interfaces.DiscountCardRepository;

import java.util.Optional;

public class DiscountCardService {

    private final DiscountCardRepository discountCardRepository;

    public DiscountCardService(DiscountCardRepository discountCardRepository) {
        this.discountCardRepository = discountCardRepository;
    }

    public Optional<DiscountCard> getDiscountCardByNumber(String cardNumber) {
        return discountCardRepository.findByNumber(cardNumber);
    }
}
