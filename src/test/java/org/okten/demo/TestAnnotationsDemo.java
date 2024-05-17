package org.okten.demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAnnotationsDemo {

    @BeforeEach
    public void setup() {
        System.out.println("setup before each");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("tear down after each");
    }

    @BeforeAll
    public static void setupAll() {
        System.out.println("setup before all");
    }

    @AfterAll
    public static void tearDownAll() {
        System.out.println("tear down after all");
    }

    @Test
    void testMethod1() {
        System.out.println("testing 1 method...");
    }

    @Test
    void testMethod2() {
        System.out.println("testing 2 method...");
    }
}
