package com.factorial;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class FileWriterThread extends Thread {

    private final String outputFile;

    private final Queue<String> resultsToProcess;

    private final int totalNumbers;

    private final Map<Integer, String> orderedResults = new ConcurrentHashMap<>();

    private int nextOrderToWrite = 0;

    public FileWriterThread(final String outputFile, final Queue<String> resultsToProcess, final int totalNumbers) {
        this.outputFile = outputFile;
        this.resultsToProcess = resultsToProcess;
        this.totalNumbers = totalNumbers;
    }

    @Override
    public void run() {
        try (FileWriter writer = new FileWriter(outputFile)) {
            while (!isInterrupted() || !resultsToProcess.isEmpty()) {
                String result = resultsToProcess.poll();
                if (result != null) {
                    String[] parts = result.split(":", 2);
                    int order = Integer.parseInt(parts[0]);
                    String formattedResult = parts[1];

                    orderedResults.put(order, formattedResult);
                    while (orderedResults.containsKey(nextOrderToWrite)) {
                        writer.write(orderedResults.remove(nextOrderToWrite) + "\n");
                        nextOrderToWrite++;
                        int progress = (int) (((double) nextOrderToWrite / totalNumbers) * 100);
                        System.out.printf("\rProgress: %d%% (%d of %d)", progress, nextOrderToWrite, totalNumbers);
                    }
                }
            }
        } catch (IOException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error writing to file: " + outputFile);
            e.printStackTrace();
        }
    }
}
