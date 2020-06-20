package io.gojek.parkinglot.processor.impl;

import io.gojek.parkinglot.services.ParkingService;
import io.gojek.parkinglot.writer.Writer;


public class FileCommandProcessor extends AbstractCommandProcessor {

    private final String fileName;

    public FileCommandProcessor(ParkingService parkingService, Writer writer, String fileName) {
        super(parkingService, writer);
        this.fileName = fileName;
    }

    public void init() {

    }

    @Override
    public void process() {

    }
}
