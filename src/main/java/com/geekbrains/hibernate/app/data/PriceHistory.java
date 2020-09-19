package com.geekbrains.hibernate.app.data;

public class PriceHistory implements Comparable<Object>{
    private String productName;
    private String date;
    private float cost;

    public PriceHistory(String productName, String date, float cost) {
        this.productName = productName;
        this.date = date;
        this.cost = cost;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    @Override
    public int compareTo(Object o) {
        PriceHistory history = (PriceHistory)o;
        return this.productName.compareTo(history.productName);
    }

    @Override
    public String toString() {
        return String.format("Product: %s, date: %s, cost: %.2f)", productName, date, cost);
    }
}
