package ru.clevertec.check.entity;

public class DiscountCard {
    private Integer id;
    private int cardNumber;
    private int discountAmount;

    public DiscountCard(Integer id, int cardNumber, int discountAmount) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.discountAmount = discountAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }
}
