package ru.clevertec.check.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.repository.interfaces.DiscountCardRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DiscountCardServiceTest {
    private DiscountCardRepository discountCardRepository;
    private DiscountCardService discountCardService;

    @BeforeEach
    void setUp() {
        discountCardRepository = mock(DiscountCardRepository.class);
        discountCardService = new DiscountCardService(discountCardRepository);
    }

    @Test
    void getAllDiscountCards() {
        DiscountCard card1 = new DiscountCard(1, 1111, 3);
        DiscountCard card2 = new DiscountCard(2, 2222, 5);
        when(discountCardRepository.findAll()).thenReturn(List.of(card1, card2));

        List<DiscountCard> cards = discountCardService.getAllDiscountCards();

        assertNotNull(cards);
        assertEquals(2, cards.size());
        assertTrue(cards.contains(card1));
        assertTrue(cards.contains(card2));
        verify(discountCardRepository, times(1)).findAll();
    }

    @Test
    void getDiscountCardByNumber_CardExist() {
        DiscountCard discountCard = new DiscountCard(1,1111,3);
        when(discountCardRepository.findByNumber(1111)).thenReturn(Optional.of(discountCard));

        Optional<DiscountCard> result = discountCardService.getDiscountCardByNumber(1111);

        assertTrue(result.isPresent());
        assertEquals(discountCard, result.get());
        verify(discountCardRepository, times(1)).findByNumber(1111);
    }

    @Test
    void getDiscountCardByNumber_CardNotExist() {
        when(discountCardRepository.findByNumber(1111)).thenReturn(Optional.empty());

        Optional<DiscountCard> result = discountCardService.getDiscountCardByNumber(1111);

        assertTrue(result.isEmpty());
        verify(discountCardRepository, times(1)).findByNumber(1111);
    }

    @Test
    void getDiscountCardById_CardExist() {
        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        when(discountCardRepository.findById(1)).thenReturn(Optional.of(discountCard));

        Optional<DiscountCard> result = discountCardService.getDiscountCardById(1);

        assertTrue(result.isPresent());
        assertEquals(discountCard, result.get());
        verify(discountCardRepository, times(1)).findById(1);
    }

    @Test
    void getDiscountCardById_CardNotExist() {
        when(discountCardRepository.findById(1)).thenReturn(Optional.empty());

        Optional<DiscountCard> result = discountCardService.getDiscountCardById(1);

        assertTrue(result.isEmpty());
        verify(discountCardRepository, times(1)).findById(1);
    }

    @Test
    void createDiscountCard() {
        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        doNothing().when(discountCardRepository).createDiscountCard(discountCard);

        discountCardService.createDiscountCard(discountCard);

        verify(discountCardRepository, times(1)).createDiscountCard(discountCard);
    }

    @Test
    void createDiscountCard_AndThenGetDiscountCardById() {
        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        doAnswer(invocation -> {
            DiscountCard card = invocation.getArgument(0);
            card.setId(1);
            return null;
        }).when(discountCardRepository).createDiscountCard(discountCard);
        when(discountCardRepository.findById(1)).thenReturn(Optional.of(discountCard));

        discountCardService.createDiscountCard(discountCard);
        Optional<DiscountCard> result = discountCardService.getDiscountCardById(1);

        assertTrue(result.isPresent());
        assertEquals(discountCard, result.get());
        verify(discountCardRepository, times(1)).createDiscountCard(discountCard);
        verify(discountCardRepository, times(1)).findById(1);
    }

    @Test
    void updateDiscountCardById_CardExist() {
        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        when(discountCardRepository.updateDiscountCardById(1, discountCard)).thenReturn(true);

        boolean result = discountCardService.updateDiscountCardById(1, discountCard);

        assertTrue(result);
        verify(discountCardRepository, times(1)).updateDiscountCardById(1, discountCard);
    }

    @Test
    void updateDiscountCardById_CardNotExist() {
        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        when(discountCardRepository.updateDiscountCardById(1, discountCard)).thenReturn(false);

        boolean result = discountCardService.updateDiscountCardById(1, discountCard);

        assertFalse(result);
        verify(discountCardRepository, times(1)).updateDiscountCardById(1, discountCard);
    }

    @Test
    void deleteDiscountCardById_CardExist() {
        when(discountCardRepository.deleteDiscountCardById(1)).thenReturn(true);

        boolean result = discountCardService.deleteDiscountCardById(1);

        assertTrue(result);
        verify(discountCardRepository, times(1)).deleteDiscountCardById(1);
    }

    @Test
    void deleteDiscountCardById_CardNotExist() {
        when(discountCardRepository.deleteDiscountCardById(1)).thenReturn(false);

        boolean result = discountCardService.deleteDiscountCardById(1);

        assertFalse(result);
        verify(discountCardRepository, times(1)).deleteDiscountCardById(1);
    }
}
