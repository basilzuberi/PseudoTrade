package com.project.pseudotrade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebsTest {

    Webs testWebs;

    @BeforeEach
    void setUp() {
        testWebs = new Webs(1, "Example Image Name", "Example Description");
    }

    @Test
    void getImage() {
        assertEquals(testWebs.getImage(), 1);
    }

    @Test
    void setImage() {
        testWebs.setImage(2);
        assertEquals(testWebs.getImage(), 2);
    }

    @Test
    void getName() {
        assertEquals(testWebs.getName(), "Example Image Name");
    }

    @Test
    void setName() {
        testWebs.setName("Name 2");
        assertEquals(testWebs.getName(), "Name 2");
    }

    @Test
    void getDes() {
        assertEquals(testWebs.getDes(), "Example Description");
    }

    @Test
    void setDes() {
        testWebs.setDes("A new description");
        assertEquals(testWebs.getDes(), "A new description");
    }
}