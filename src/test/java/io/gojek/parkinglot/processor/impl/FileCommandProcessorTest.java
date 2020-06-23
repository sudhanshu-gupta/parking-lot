package io.gojek.parkinglot.processor.impl;


import io.gojek.parkinglot.processor.CommandProcessor;
import io.gojek.parkinglot.services.impl.ParkingServiceImpl;
import io.gojek.parkinglot.writer.impl.CommandLinePrintWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileCommandProcessorTest {

    private CommandProcessor commandProcessor;

    @Before
    public void setUp() {
        commandProcessor = new FileCommandProcessor(
                new ParkingServiceImpl(new ReentrantReadWriteLock()), new CommandLinePrintWriter(),
                new File("src/test/resources/test_file_input.txt").getAbsolutePath());
    }

    @Test
    public void should_process_when_validFileCommands() {
        commandProcessor.process();
    }
}