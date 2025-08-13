package com.factorial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class FactorialApp {

    public static void main(String[] args) {
        final Queue<String> resultsToWrite = new LinkedBlockingQueue<>();
        final Scanner scanner = new Scanner(System.in);
        int threadPoolSize;

        try {
            System.out.print("Enter the size of the calculation thread pool: ");
            threadPoolSize = scanner.nextInt();
            if (threadPoolSize <= 0) {
                System.out.println("The pool size must be a positive number");
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number");
            return;
        }

        int totalNumbers = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            while (reader.readLine() != null) {
                totalNumbers++;
            }
        } catch (IOException e) {
            System.err.println("Error reading input.txt to count total numbers");
            return;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

        final FileReaderThread readerThread = new FileReaderThread("input.txt", executorService, resultsToWrite);
        readerThread.start();

        final FileWriterThread writerThread = new FileWriterThread("output.txt", resultsToWrite, totalNumbers);
        writerThread.start();

        try {
            readerThread.join();

            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            writerThread.interrupt();
            writerThread.join();

            System.out.println("\nAll tasks completed. Results are in output.txt.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}