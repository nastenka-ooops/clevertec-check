package ru.clevertec.check.entity;

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
}
