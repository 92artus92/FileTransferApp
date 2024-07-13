package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {

        AppConfig appConfig = new AppConfig();
        File input_file = new File(appConfig.getSourceFilePath());
        File output_file_whole = new File(appConfig.getOutputFileWhole());
        File output_file_batch = new File(appConfig.getOutputFileBatch());
        int batchSize = appConfig.getBatchSize();
        long start_time;

        //Transfer whole file
        start_time = System.nanoTime();
        transferWholeFile(input_file, output_file_whole);
        timeCounter("Time taken (whole file): %d ms. \n", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start_time));

        //Transfer in batch
        start_time = System.nanoTime();
        transferBatchFile(input_file, output_file_batch, batchSize);
        timeCounter("Time taken (batch): %d ms.", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start_time));
    }

    static void transferWholeFile(File inputFile, File output_file_whole) throws IOException {
        try (FileChannel sourceChannel = new FileInputStream(inputFile).getChannel();
             FileChannel targetChannel = new FileOutputStream(output_file_whole).getChannel()) {
            targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
    }

    static void transferBatchFile(File inputFile, File outputFileBatch, int batchSize) throws IOException {

        if (batchSize <= 0) {
            throw new IllegalArgumentException("Batch size must be greater than zero");
        }

        try (FileChannel sourceChannel = new FileInputStream(inputFile).getChannel();
             FileChannel targetChannel = new FileOutputStream(outputFileBatch).getChannel()) {

            long size = sourceChannel.size();
            long position = 0;
            while (position < size) {
                long transferredBytes = sourceChannel.transferTo(position, batchSize, targetChannel);
                position += transferredBytes;
            }
        }
    }

    private static void timeCounter(String text, long duration) {
        System.out.printf(text, duration);
    }
}