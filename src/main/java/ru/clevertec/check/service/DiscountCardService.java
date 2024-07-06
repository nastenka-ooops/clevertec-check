package ru.clevertec.check.service;

import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.repository.interfaces.DiscountCardRepository;

import java.util.Optional;

public class DiscountCardService {

    private final DiscountCardRepository discountCardRepository;

    public DiscountCardService(DiscountCardRepository discountCardRepository) {
        this.discountCardRepository = discountCardRepository;
    }

    public Optional<DiscountCard> getDiscountCardByNumber(int cardNumber) {
        return discountCardRepository.findByNumber(cardNumber);
    }
}
