package ru.clevertec.check.service;

import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.repository.interfaces.DiscountCardRepository;

import java.util.List;
import java.util.Optional;

public class DiscountCardService {

    private final DiscountCardRepository discountCardRepository;

    public DiscountCardService(DiscountCardRepository discountCardRepository) {
        this.discountCardRepository = discountCardRepository;
    }

    public List<DiscountCard> getAllDiscountCards() {
        return discountCardRepository.findAll();
    }

    public Optional<DiscountCard> getDiscountCardByNumber(int cardNumber) {
        return discountCardRepository.findByNumber(cardNumber);
    }

    public Optional<DiscountCard> getDiscountCardById(int id) {
        return discountCardRepository.findById(id);
    }

    public void createDiscountCard(DiscountCard discountCard) {
        discountCardRepository.createDiscountCard(discountCard);
    }

    public boolean updateDiscountCardById(int id, DiscountCard discountCard) {
        return discountCardRepository.updateDiscountCardById(id, discountCard);
    }

    public boolean deleteDiscountCardById(int id) {
        return discountCardRepository.deleteDiscountCardById(id);
    }
}
