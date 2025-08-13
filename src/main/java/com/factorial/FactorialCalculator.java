package com.factorial;

import java.math.BigInteger;
import java.util.Queue;

public class FactorialCalculator implements Runnable {

    private static final int MAX_CALCULATIONS_PER_SECOND = 100;

    private static final Object LOCK = new Object();

    private static int calculationCounter = 0;

    private static long startTime = System.currentTimeMillis();

    private final int originalOrder;

    private final int number;

    private final Queue<String> resultsToWrite;

    public FactorialCalculator(final int originalOrder, final int number, final Queue<String> resultsToWrite) {
        this.originalOrder = originalOrder;
        this.number = number;
        this.resultsToWrite = resultsToWrite;
    }

    BigInteger calculateFactorial(final int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    @Override
    public void run() {
        synchronized (LOCK) {
            final long currentTime = System.currentTimeMillis();
            if (calculationCounter >= MAX_CALCULATIONS_PER_SECOND) {
                final long elapsedTime = currentTime - startTime;
                if (elapsedTime < 1000) {
                    try {
                        Thread.sleep(1000 - elapsedTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                startTime = System.currentTimeMillis();
                calculationCounter = 0;
            }
            calculationCounter++;
        }

        final BigInteger result = calculateFactorial(number);
        resultsToWrite.add(String.format("%d:%d=%s", originalOrder, number, result.toString()));
    }
}