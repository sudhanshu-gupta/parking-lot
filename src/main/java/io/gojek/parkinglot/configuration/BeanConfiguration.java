package io.gojek.parkinglot.configuration;

import io.gojek.parkinglot.processor.impl.FileCommandProcessor;
import io.gojek.parkinglot.processor.impl.InteractiveCommandProcessor;
import io.gojek.parkinglot.processor.strategy.ProcessStrategy;
import io.gojek.parkinglot.services.ParkingService;
import io.gojek.parkinglot.services.impl.ParkingServiceImpl;
import io.gojek.parkinglot.writer.Writer;
import io.gojek.parkinglot.writer.impl.CommandLineWriter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BeanConfiguration {

    public FileCommandProcessor fileCommandProcessor(String fileName) {
        return new FileCommandProcessor(parkingService(), commandLineWriter(), fileName);
    }

    public InteractiveCommandProcessor interactiveCommandProcessor() {
        return new InteractiveCommandProcessor(parkingService(), commandLineWriter());
    }

    public ParkingService parkingService() {
        return new ParkingServiceImpl();
    }

    public Writer commandLineWriter() {
        return new CommandLineWriter();
    }
}
