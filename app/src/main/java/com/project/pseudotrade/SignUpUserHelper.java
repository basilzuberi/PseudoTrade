package com.project.pseudotrade;

import java.util.HashMap;

public class SignUpUserHelper {

    String email,username;
    Double cashBalance;
    HashMap<String, Integer> holdings;
    public SignUpUserHelper() {

    }
    public SignUpUserHelper(String email, String username) {
        this.email = email;
        this.username = username;
        this.cashBalance = 1000.00;
        this.holdings = new HashMap<String, Integer>();
        this.holdings.put("PlaceholderStock", -1);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getCashBalance() { return this.cashBalance; }

    public void setCashBalance(Double cashBalance) { this.cashBalance = cashBalance; }

    public HashMap<String, Integer> getHoldings() { return this.holdings; }

    public void setHoldings(HashMap<String, Integer> holdings) { this.holdings = holdings;}
}
