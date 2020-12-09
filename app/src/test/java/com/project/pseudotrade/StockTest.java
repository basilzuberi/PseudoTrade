package com.project.pseudotrade;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    Stock testStock;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        testStock = new Stock("AAPL", "Apple Inc.", 5, 120.00);
    }

    @org.junit.jupiter.api.Test
    void getTicker() {
        assertEquals(testStock.getTicker(), "AAPL");
    }

    @org.junit.jupiter.api.Test
    void getName() {
        assertEquals(testStock.getName(), "Apple Inc.");
    }

    @org.junit.jupiter.api.Test
    void getQuantity() {
        assertEquals(testStock.getQuantity(), 5);
    }

    @org.junit.jupiter.api.Test
    void getCurrentPrice() {
        assertEquals(testStock.getCurrentPrice(), 120.00);
    }

    @org.junit.jupiter.api.Test
    void getHoldingsValue() {
        assertEquals(testStock.getHoldingsValue(), 600.00);
    }
}