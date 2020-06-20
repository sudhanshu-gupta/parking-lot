package io.gojek.parkinglot;

import io.gojek.parkinglot.processor.CommandProcessor;
import io.gojek.parkinglot.processor.strategy.ProcessStrategy;

public class Application {

    public static void main(String[] args) {

        CommandProcessor processor = ProcessStrategy.getProcessor(args);
        processor.process();
    }

}
