package io.gojek.parkinglot.writer.impl;


import io.gojek.parkinglot.writer.PrintWriter;
import org.junit.Before;
import org.junit.Test;

public class CommandLinePrintWriterTest {

    private PrintWriter printWriter;

    @Before
    public void setUp() {
        printWriter = new CommandLinePrintWriter();
    }

    @Test
    public void should_print_when_validText() {
        printWriter.print("Sorry, No Car Available");
    }

    @Test
    public void should_print_when_variablesProvided() {
        printWriter.print("Total Slots available: {}", 2);
    }

    @Test
    public void should_print_when_variablesNotProvided() {
        printWriter.print("Total Slots available: {}");
    }
}