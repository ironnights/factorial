package com.factorial;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FactorialCalculatorTest {

    @Test
    public void testFactorialOfZero() {
        FactorialCalculator calculator = new FactorialCalculator(0, 0, null);
        BigInteger expected = BigInteger.ONE;
        BigInteger result = calculator.calculateFactorial(0);
        assertEquals(expected, result);
    }

    @Test
    void testFactorialOfOne() {
        FactorialCalculator calculator = new FactorialCalculator(0, 1, null);
        BigInteger expected = BigInteger.ONE;
        BigInteger result = calculator.calculateFactorial(1);
        assertEquals(expected, result);
    }

    @Test
    void testFactorialOfPositiveNumber() {
        FactorialCalculator calculator = new FactorialCalculator(0, 5, null);
        BigInteger expected = new BigInteger("120");
        BigInteger result = calculator.calculateFactorial(5);
        assertEquals(expected, result);
    }

    @Test
    void testFactorialOfLargeNumber() {
        FactorialCalculator calculator = new FactorialCalculator(0, 20, null);
        BigInteger expected = new BigInteger("2432902008176640000");
        BigInteger result = calculator.calculateFactorial(20);
        assertEquals(expected, result);
    }

    @Test
    void testFactorialOfNegativeNumber() {
        FactorialCalculator calculator = new FactorialCalculator(0, -5, null);
        BigInteger expected = BigInteger.ONE;
        BigInteger result = calculator.calculateFactorial(-5);
        assertEquals(expected, result);
    }
}

