package org.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainTest {

    private File inputFile;
    private File outputFileWhole;
    private File outputFileBatch;
    private int batchSize;

    @Before
    public void setup() {

        //Configuration
        AppConfig appConfig = new AppConfig();
        inputFile = new File(appConfig.getSourceFilePath());
        outputFileWhole = new File(appConfig.getOutputFileWhole());
        outputFileBatch = new File(appConfig.getOutputFileBatch());
        batchSize = appConfig.getBatchSize();
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(outputFileWhole.toPath());
        Files.deleteIfExists(outputFileBatch.toPath());
    }

    @Test
    public void testTransferWholeFileIfExists() throws IOException {

        //Remove remaining file
        Files.deleteIfExists(Paths.get(outputFileWhole.getPath()));

        Main.transferWholeFile(inputFile, outputFileWhole);

        assertTrue(outputFileWhole.exists());
        assertTrue(outputFileWhole.length() > 0);

        String inputContent = new String(Files.readAllBytes(inputFile.toPath()));
        String outputContentWhole = new String(Files.readAllBytes(outputFileWhole.toPath()));
        assertEquals(inputContent, outputContentWhole);
    }

    @Test
    public void testTransferBatchFileIfExists() throws IOException {

        Files.deleteIfExists(Paths.get(outputFileBatch.getPath()));

        Main.transferBatchFile(inputFile, outputFileBatch, batchSize);

        assertTrue(outputFileBatch.exists());
        assertTrue(outputFileBatch.length() > 0);

        String inputContent = new String(Files.readAllBytes(inputFile.toPath()));
        String outputContentBatch = new String(Files.readAllBytes(outputFileBatch.toPath()));
        assertEquals(inputContent, outputContentBatch);
    }

    @Test(expected = FileNotFoundException.class)
    public void testTransferWholeFileIfNonExistenInputFile() throws IOException {

        Files.deleteIfExists(Paths.get(outputFileWhole.getPath()));

        Main.transferWholeFile(new File("test.csv"), outputFileWhole);
    }

    @Test(expected = FileNotFoundException.class)
    public void testTransferBatchFileIfNonExistenInputFile() throws IOException {

        Files.deleteIfExists(Paths.get(outputFileBatch.getPath()));

        Main.transferBatchFile(new File("test.csv"), outputFileBatch, batchSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroBatchSize() throws IOException {
        Main.transferBatchFile(inputFile, outputFileBatch, 0);
    }
}