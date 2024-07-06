package ru.clevertec.check.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.repository.interfaces.DiscountCardRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class DiscountCardTest {
    private DiscountCardRepository discountCardRepository;
    private DiscountCardService discountCardService;

    @BeforeEach
    void setUp() {
        discountCardRepository = mock(DiscountCardRepository.class);
        discountCardService = new DiscountCardService(discountCardRepository);
    }

    @Test
    void getDiscountCard_CardExist() {
        DiscountCard discountCard = new DiscountCard(1,1111,3);
        when(discountCardRepository.findByNumber(1111)).thenReturn(Optional.of(discountCard));

        Optional<DiscountCard> result = discountCardService.getDiscountCardByNumber(1111);

        assertTrue(result.isPresent());
        assertEquals(discountCard, result.get());
        verify(discountCardRepository, times(1)).findByNumber(1111);
    }

    @Test
    void getDiscountCard_CardNotExist() {
        when(discountCardRepository.findByNumber(1111)).thenReturn(Optional.empty());

        Optional<DiscountCard> result = discountCardService.getDiscountCardByNumber(1111);

        assertTrue(result.isEmpty());
        verify(discountCardRepository, times(1)).findByNumber(1111);
    }
}
