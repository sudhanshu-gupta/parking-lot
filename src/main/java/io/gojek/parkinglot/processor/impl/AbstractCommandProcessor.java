package io.gojek.parkinglot.processor.impl;

import io.gojek.parkinglot.objects.Car;
import io.gojek.parkinglot.objects.Constants;
import io.gojek.parkinglot.processor.commands.ParkingLotCommand;
import io.gojek.parkinglot.utils.Assert;
import io.gojek.parkinglot.writer.Writer;
import io.gojek.parkinglot.processor.CommandProcessor;
import io.gojek.parkinglot.services.ParkingService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public abstract class AbstractCommandProcessor implements CommandProcessor {

    private final ParkingService parkingService;
    private final Writer writer;

    protected void process(String input) {
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
            writer.write(e.getMessage());
        }
    }

    private void createLot(String[] args) {
        Assert.equals(args.length, 2, "Invalid capacity");
        int capacity = Integer.valueOf(args[1].trim());
        Assert.greaterThanOrEqualTo(capacity, 1, "Invalid capacity");
        parkingService.createParkingLot(capacity);
        writer.write("Created a parking lot with {} slots", capacity);
    }

    private void park(String[] args) {
        Assert.equals(args.length, 3, "Invalid input to park car");
        int slot = parkingService.park(new Car(args[1].trim().toUpperCase(), args[2].trim()));
        if(slot == Constants.NOT_AVAILABLE) {
            writer.write("Sorry, parking lot is full");
        }
        else if(slot == Constants.ALREADY_EXIST) {
            writer.write("Sorry, vehicle is already parked.");
        } else {
            writer.write("Allocated slot number: {}", slot);
        }
    }

    private void leave(String[] args) {
        Assert.equals(args.length, 2, "Invalid input to unpark car");
        int slot = Integer.valueOf(args[1].trim());
        Assert.greaterThanOrEqualTo(slot, 1, "Invalid slot number");
        if(parkingService.unPark(slot)) {
            writer.write("Slot number {} is free", slot);
        } else {
            writer.write("Slot number {} is empty", slot);
        }
    }

    private void status() {
        List<String> status = parkingService.getStatus();
        if(status == null || status.isEmpty()) {
            writer.write("Sorry, parking lot is empty.");
        }
        else {
            writer.write("Slot No.\tRegistration No\tColour");
            status.forEach(writer::write);
        }
    }

    private void getCarsByColor(String[] args) {
        Assert.equals(args.length, 2, "Invalid colour input");
        List<String> registrationNumbers = parkingService.getRegistrationNumbersByColor(args[1].trim());
        if(registrationNumbers == null || registrationNumbers.isEmpty()) {
            writer.write("Sorry, no cars with colour: {} found", args[1]);
        }
        else {
            writer.write(registrationNumbers.stream().collect(Collectors.joining(", ")).trim());
        }
    }

    private void getSlotsByColor(String[] args) {
        Assert.equals(args.length, 2, "Invalid colour input");
        List<Integer> slots = parkingService.getSlotsByColor(args[1].trim());
        if(slots == null || slots.isEmpty()) {
            writer.write("Sorry, no slots with cars of colour: {} found", args[1]);
        }
        else {
            writer.write(slots.stream().map(String::valueOf).collect(Collectors.joining(", ")).trim());
        }
    }

    private void getSlotByRegistrationNumber(String[] args) {
        Assert.equals(args.length, 2, "Invalid registration number");
        int slotNo = parkingService.getSlotNumberByRegistrationNumber(args[1].trim());
        if(slotNo == Constants.NOT_FOUND) {
            writer.write("Not found");
        }
        else {
            writer.write("{}", slotNo);
        }
    }
}
