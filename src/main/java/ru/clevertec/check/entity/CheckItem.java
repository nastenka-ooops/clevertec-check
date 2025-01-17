package main.java.ru.clevertec.check.entity;

public class CheckItem {
    private Product product;
    private int quantity;
    private double totalPrice;
    private double discount;

    public CheckItem(Product product, int quantity, double totalPrice, double discount) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.discount = discount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
