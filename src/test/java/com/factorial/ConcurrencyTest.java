package com.factorial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConcurrencyTest {

    @TempDir
    Path tempDir;

    @Test
    void testConcurrencyAndOrder() throws IOException, InterruptedException {
        File inputFile = tempDir.resolve("input.txt").toFile();
        File outputFile = tempDir.resolve("output.txt").toFile();

        List<String> inputLines = new ArrayList<>();
        int totalNumbers = 1200;
        for (int i = 0; i < totalNumbers; i++) {
            inputLines.add(String.valueOf(i));
        }
        Files.write(inputFile.toPath(), inputLines);

        int threadPoolSize = 6;
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

        Queue<String> resultsToWrite = new LinkedBlockingQueue<>();

        FileReaderThread readerThread = new FileReaderThread(inputFile.getAbsolutePath(), executorService, resultsToWrite);
        readerThread.start();

        FileWriterThread writerThread = new FileWriterThread(outputFile.getAbsolutePath(), resultsToWrite, totalNumbers);
        writerThread.start();

        readerThread.join();

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        writerThread.interrupt();
        writerThread.join();

        List<String> outputLines = Files.readAllLines(outputFile.toPath());

        assertEquals(totalNumbers, outputLines.size(), "Output file should contain the same number of lines as input.");

        for (int i = 0; i < totalNumbers; i++) {
            String expectedLine = String.format("%d=%s", i, calculateFactorial(i).toString());
            String[] parts = outputLines.get(i).split("=", 2);
            String actualResult = parts[1];
            assertEquals(expectedLine, i + "=" + actualResult, "Result at line " + i + " is not in the correct order or value.");
        }
    }

    private BigInteger calculateFactorial(int n) {
        if (n < 0) return BigInteger.ONE;
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
}

