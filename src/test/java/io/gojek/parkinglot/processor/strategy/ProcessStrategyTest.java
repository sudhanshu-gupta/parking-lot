package io.gojek.parkinglot.processor.strategy;

import io.gojek.parkinglot.processor.CommandProcessor;
import io.gojek.parkinglot.processor.impl.FileCommandProcessor;
import io.gojek.parkinglot.processor.impl.InteractiveCommandProcessor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcessStrategyTest {

    @Test
    public void should_returnFileCommandProcessStrategy_when_firstInputPresent() {
        CommandProcessor processor = ProcessStrategy.getProcessor(new String[]{"input.txt"});
        assertThat(processor).isInstanceOf(FileCommandProcessor.class);
    }

    @Test
    public void should_returnInteractiveCommandProcessStrategy_when_inputAbsent() {
        CommandProcessor processor = ProcessStrategy.getProcessor(new String[0]);
        assertThat(processor).isInstanceOf(InteractiveCommandProcessor.class);
    }
}