package io.gojek.parkinglot.processor.commands;

import io.gojek.parkinglot.exceptions.IllegalCommandException;
import io.gojek.parkinglot.utils.Assert;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
public enum ParkingLotCommand {

    CREATE("create_parking_lot"),
    PARK("park"),
    LEAVE("leave"),
    STATUS("status"),
    GET_CARS_BY_COLOR("registration_numbers_for_cars_with_colour"),
    GET_SLOTS_BY_COLOR("slot_numbers_for_cars_with_colour"),
    GET_SLOT_BY_REG_NO("slot_number_for_registration_number"),
    EXIT("exit");

    private final String name;

    private static final Map<String, ParkingLotCommand> COMMANDS = Collections.unmodifiableMap(initialize());

    public static ParkingLotCommand get(String command) {
        Assert.notEmpty(command, "command cannot be null or empty");
        return Optional.ofNullable(COMMANDS.get(command.trim().toLowerCase())).orElseThrow(IllegalCommandException::new);
    }

    private static Map<String, ParkingLotCommand> initialize() {
        return Stream.of(ParkingLotCommand.values()).collect(Collectors.toMap(a -> a.name, a -> a));
    }
}
