package io.gojek.parkinglot;

import io.gojek.parkinglot.processor.strategy.ProcessStrategy;

public class Application {

    public static void main(String[] args) {

        ProcessStrategy.getProcessor(args).process();
    }

}
