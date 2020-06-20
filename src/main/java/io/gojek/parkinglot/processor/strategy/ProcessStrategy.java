package io.gojek.parkinglot.processor.strategy;

import io.gojek.parkinglot.configuration.BeanConfiguration;
import io.gojek.parkinglot.processor.CommandProcessor;
import io.gojek.parkinglot.utils.Assert;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProcessStrategy {

    public CommandProcessor getProcessor(String[] args) {
        if (args.length > 0) {
            Assert.notEmpty(args[0], "input file name cannot null or empty");
            return BeanConfiguration.fileCommandProcessor(args[0]);
        }
        return BeanConfiguration.interactiveCommandProcessor();
    }
}
