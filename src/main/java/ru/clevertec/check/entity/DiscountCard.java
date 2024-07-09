package ru.clevertec.check.entity;

import java.util.Objects;

public class DiscountCard {
    private Integer id;
    private int discountCard;
    private int discountAmount;

    public DiscountCard() {
    }

    public DiscountCard(Integer id, int discountCard, int discountAmount) {
        this.id = id;
        this.discountCard = discountCard;
        this.discountAmount = discountAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(int discountCard) {
        this.discountCard = discountCard;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountCard card = (DiscountCard) o;
        return Objects.equals(id, card.id) &&
                discountCard == card.discountCard &&
                discountAmount == card.discountAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, discountCard, discountAmount);
    }
}
