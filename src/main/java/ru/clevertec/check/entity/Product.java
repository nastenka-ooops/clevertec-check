package ru.clevertec.check.entity;

public class Product {

    private Integer id;
    private String description;
    private double price;
    private int quantity;
    private boolean isWholesale;

    public Product() {
    }

    public Product(Integer id, String description, double price, int quantity, boolean isWholesale) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.isWholesale = isWholesale;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean getIsWholesale() {
        return isWholesale;
    }

    public void setIsWholesale(boolean wholesale) {
        this.isWholesale = wholesale;
    }
}
