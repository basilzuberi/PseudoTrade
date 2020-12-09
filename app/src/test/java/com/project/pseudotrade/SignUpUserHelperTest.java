package com.project.pseudotrade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SignUpUserHelperTest {
    SignUpUserHelper signUpUserHelper = new SignUpUserHelper("test@pt.ca","testUser");

//    signUpUserHelper = new SignUpUserHelper("test@pt.ca","testUser");
    @BeforeEach
    void setUp() {
        HashMap<String, Integer> dummyHoldings = new HashMap<>();
        dummyHoldings.put("TestStock",6);
        signUpUserHelper.setHoldings(dummyHoldings);
        signUpUserHelper.setCashBalance(1000.00);
    }

    @Test
    void getEmail() {
        assertEquals("test@pt.ca",signUpUserHelper.getEmail());
    }

    @Test
    void setEmail() {
        signUpUserHelper.setEmail("test@bt.ca");
        assertEquals("test@bt.ca",signUpUserHelper.getEmail());
    }

    @Test
    void getUsername() {
        assertEquals("testUser",signUpUserHelper.getUsername());
    }

    @Test
    void setUsername() {
        signUpUserHelper.setUsername("testUserTwo");
        assertEquals("testUserTwo",signUpUserHelper.getUsername());
    }

    @Test
    void getCashBalance() {
        assertEquals(1000.00,signUpUserHelper.getCashBalance());
    }

    @Test
    void setCashBalance() {
        signUpUserHelper.setCashBalance(1500.00);
        assertEquals(1500.00,signUpUserHelper.getCashBalance());
    }

    @Test
    void getHoldings() {
        HashMap<String, Integer> dummyTest = new HashMap<>();
        dummyTest.put("TestStock",6);
        assertEquals(dummyTest,signUpUserHelper.getHoldings());
    }

    @Test
    void setHoldings() {
        HashMap<String, Integer> dummyTest = new HashMap<>();
        dummyTest.put("TestStock",6);
        dummyTest.put("TestStockTwo",5);
        signUpUserHelper.setHoldings(dummyTest);
        assertEquals(dummyTest,signUpUserHelper.getHoldings());
    }
}