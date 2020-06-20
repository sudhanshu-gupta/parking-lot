package io.gojek.parkinglot.processor.impl;

import io.gojek.parkinglot.services.ParkingService;
import io.gojek.parkinglot.writer.Writer;

import java.io.InputStreamReader;

public class InteractiveCommandProcessor extends AbstractCommandProcessor {

    public InteractiveCommandProcessor(ParkingService parkingService, Writer writer) {
        super(parkingService, writer);
    }

    @Override
    public void process() {
        process(new InputStreamReader(System.in));
    }
}
