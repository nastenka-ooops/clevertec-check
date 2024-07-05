package main.java.ru.clevertec.check.entity;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class Check {
    private Date date;
    private Time time;
    private List<CheckItem> checkItems;
    private double totalPrice;
    private double totalDiscount;
    private double totalPriceWithDiscount;

    public Check(Date date, Time time, List<CheckItem> checkItems, double totalPrice, double totalDiscount, double totalPriceWithDiscount) {
        this.date = date;
        this.time = time;
        this.checkItems = checkItems;
        this.totalPrice = totalPrice;
        this.totalDiscount = totalDiscount;
        this.totalPriceWithDiscount = totalPriceWithDiscount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public List<CheckItem> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<CheckItem> checkItems) {
        this.checkItems = checkItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotalPriceWithDiscount() {
        return totalPriceWithDiscount;
    }

    public void setTotalPriceWithDiscount(double totalPriceWithDiscount) {
        this.totalPriceWithDiscount = totalPriceWithDiscount;
    }
}
