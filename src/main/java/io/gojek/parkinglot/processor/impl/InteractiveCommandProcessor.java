package io.gojek.parkinglot.processor.impl;

import io.gojek.parkinglot.services.ParkingService;
import io.gojek.parkinglot.writer.PrintWriter;

import java.io.InputStreamReader;

public class InteractiveCommandProcessor extends AbstractCommandProcessor {

    private final PrintWriter printWriter;
    public InteractiveCommandProcessor(ParkingService parkingService, PrintWriter printWriter) {
        super(parkingService, printWriter);
        this.printWriter = printWriter;
    }

    /**
     * run the parking lot process. It read from the command prompt. On receiving exit command, it terminate the program
     */
    @Override
    public void process() {
        printWriter.print("Welcome to Parking Lot System. Please enter any command to continue... or type 'exit' to terminate...");
        process(new InputStreamReader(System.in));
    }
}
