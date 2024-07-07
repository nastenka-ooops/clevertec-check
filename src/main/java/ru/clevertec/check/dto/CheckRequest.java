package ru.clevertec.check.dto;

import java.util.List;

public class CheckRequest {
    private List<ProductRequest> products;
    private int discountCard;
    private double balanceDebitCard;

    public CheckRequest() {
    }

    public CheckRequest(List<ProductRequest> products, int discountCard, double balanceDebitCard) {
        this.products = products;
        this.discountCard = discountCard;
        this.balanceDebitCard = balanceDebitCard;
    }

    public List<ProductRequest> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRequest> products) {
        this.products = products;
    }

    public int getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(int discountCard) {
        this.discountCard = discountCard;
    }

    public double getBalanceDebitCard() {
        return balanceDebitCard;
    }

    public void setBalanceDebitCard(double balanceDebitCard) {
        this.balanceDebitCard = balanceDebitCard;
    }
}
