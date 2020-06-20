package io.gojek.parkinglot.processor.impl;

import io.gojek.parkinglot.services.ParkingService;
import io.gojek.parkinglot.writer.PrintWriter;

import java.io.InputStreamReader;

public class InteractiveCommandProcessor extends AbstractCommandProcessor {

    public InteractiveCommandProcessor(ParkingService parkingService, PrintWriter printWriter) {
        super(parkingService, printWriter);
    }

    @Override
    public void process() {
        process(new InputStreamReader(System.in));
    }
}
