package com.project.pseudotrade;

public class Stock {
    private final String ticker;
    private final String name;
    private final int quantity;
    private final double currentPrice;
    private final double holdingsValue;

    public Stock(String ticker, String name, int quantity, double currentPrice) {
        this.ticker = ticker;
        this.name = name;
        this.quantity = quantity;
        this.currentPrice = currentPrice;
        this.holdingsValue = this.quantity * this.currentPrice;
    }

    public String getTicker() { return this.ticker; }

    public String getName() { return this.name; }

    public int getQuantity() { return this.quantity; }

    public double getCurrentPrice() { return this.currentPrice; }

    public double getHoldingsValue() { return this.holdingsValue; }
}
