package io.gojek.parkinglot.processor.impl;

import io.gojek.parkinglot.model.Car;
import io.gojek.parkinglot.model.Constants;
import io.gojek.parkinglot.model.Vehicle;
import io.gojek.parkinglot.processor.CommandProcessor;
import io.gojek.parkinglot.processor.commands.ParkingLotCommand;
import io.gojek.parkinglot.services.ParkingService;
import io.gojek.parkinglot.utils.Assert;
import io.gojek.parkinglot.writer.PrintWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public abstract class AbstractCommandProcessor implements CommandProcessor {

    private static final String STATUS_PLACEHOLDER = "%-12s%-19s%s";
    private final ParkingService parkingService;
    private final PrintWriter printWriter;

    @SneakyThrows
    protected void process(Reader reader) {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                processCommand(line.trim());
            }
        }
    }

    private void processCommand(String input) {
        try {
            String[] args = input.split(" ");
            ParkingLotCommand command = ParkingLotCommand.get(args[0]);
            switch (command) {
                case CREATE: {
                    createLot(args);
                    break;
                }
                case PARK: {
                    park(args);
                    break;
                }
                case LEAVE: {
                    leave(args);
                    break;
                }
                case STATUS: {
                    status();
                    break;
                }
                case GET_CARS_BY_COLOR: {
                    getCarsByColor(args);
                    break;
                }
                case GET_SLOTS_BY_COLOR: {
                    getSlotsByColor(args);
                    break;
                }
                case GET_SLOT_BY_REG_NO: {
                    getSlotByRegistrationNumber(args);
                    break;
                }
                case EXIT: {
                    System.exit(0);
                    break;
                }
            }
        } catch (Exception e) {
            printWriter.print(e.getMessage());
        }
    }

    private void createLot(String[] args) {
        Assert.equals(args.length, 2, "Invalid capacity");
        int capacity = Integer.parseInt(args[1].trim());
        Assert.greaterThanOrEqualTo(capacity, 1, "Invalid capacity");
        parkingService.createParkingLot(capacity);
        printWriter.print("Created a parking lot with {} slots", capacity);
    }

    private void park(String[] args) {
        Assert.equals(args.length, 3, "Invalid input to park car");
        int slot = parkingService.park(new Car(args[1].trim().toUpperCase(), args[2].trim()));
        if (slot == Constants.NOT_AVAILABLE) {
            printWriter.print("Sorry, parking lot is full");
        } else if (slot == Constants.ALREADY_EXIST) {
            printWriter.print("Sorry, vehicle is already parked.");
        } else {
            printWriter.print("Allocated slot number: {}", slot);
        }
    }

    private void leave(String[] args) {
        Assert.equals(args.length, 2, "Invalid input to unpark car");
        int slot = Integer.parseInt(args[1].trim());
        Assert.greaterThanOrEqualTo(slot, 1, "Invalid slot number");
        if (parkingService.leave(slot)) {
            printWriter.print("Slot number {} is free", slot);
        } else {
            printWriter.print("Slot number {} is empty", slot);
        }
    }

    private void status() {
        Map<Integer, Vehicle> status = parkingService.getAllOccupiedSlots();
        if (status == null || status.isEmpty()) {
            printWriter.print("Sorry, parking lot is empty.");
        } else {
            printWriter.print(String.format(STATUS_PLACEHOLDER, "Slot No.", "Registration No", "Colour"));
            status.entrySet().stream()
                    .map(entry -> String.format(STATUS_PLACEHOLDER, entry.getKey(),
                            entry.getValue().getRegistrationNo(), entry.getValue().getColor()))
                    .forEach(printWriter::print);
        }
    }

    private void getCarsByColor(String[] args) {
        Assert.equals(args.length, 2, "Invalid colour input");
        List<String> registrationNumbers = parkingService.getRegistrationNumbersByColor(args[1].trim());
        if (registrationNumbers == null || registrationNumbers.isEmpty()) {
            printWriter.print("Sorry, no cars with colour: {} found", args[1]);
        } else {
            printWriter.print(String.join(", ", registrationNumbers).trim());
        }
    }

    private void getSlotsByColor(String[] args) {
        Assert.equals(args.length, 2, "Invalid colour input");
        List<Integer> slots = parkingService.getSlotsByColor(args[1].trim());
        if (slots == null || slots.isEmpty()) {
            printWriter.print("Sorry, no slots with cars of colour: {} found", args[1]);
        } else {
            printWriter.print(slots.stream().map(String::valueOf).collect(Collectors.joining(", ")).trim());
        }
    }

    private void getSlotByRegistrationNumber(String[] args) {
        Assert.equals(args.length, 2, "Invalid registration number");
        int slotNo = parkingService.getSlotNumberByRegistrationNumber(args[1].trim());
        if (slotNo == Constants.NOT_FOUND) {
            printWriter.print("Not found");
        } else {
            printWriter.print("{}", slotNo);
        }
    }
}
