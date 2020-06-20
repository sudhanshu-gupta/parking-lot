package io.gojek.parkinglot.processor.impl;

import io.gojek.parkinglot.services.ParkingService;
import io.gojek.parkinglot.utils.Assert;
import io.gojek.parkinglot.writer.Writer;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileReader;


public class FileCommandProcessor extends AbstractCommandProcessor {

    private final String fileName;

    public FileCommandProcessor(ParkingService parkingService, Writer writer, String fileName) {
        super(parkingService, writer);
        Assert.notEmpty(fileName, "file name cannot be null");
        this.fileName = fileName;
    }

    @Override
    @SneakyThrows
    public void process() {
        process(new FileReader(new File(fileName)));
    }
}
