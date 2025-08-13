package com.factorial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class FileReaderThread extends Thread {

    private final String inputFile;

    private final ExecutorService executorService;

    private final Queue<String> resultsToWrite;

    public FileReaderThread(
            final String inputFile,
            final ExecutorService executorService,
            final Queue<String> resultsToWrite
    ) {
        this.inputFile = inputFile;
        this.executorService = executorService;
        this.resultsToWrite = resultsToWrite;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int order = 0;
            while ((line = reader.readLine()) != null) {
                try {
                    int number = Integer.parseInt(line.trim());
                    executorService.submit(new FactorialCalculator(order++, number, resultsToWrite));
                } catch (NumberFormatException e) {
                    System.err.println("Warning: Skipping invalid number in file: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + inputFile);
            e.printStackTrace();
        }
    }
}
