package io.gojek.parkinglot.services;

import io.gojek.parkinglot.model.Vehicle;

import java.util.List;
import java.util.Map;

public interface ParkingService {

    void createParkingLot(int capacity);

    String park(Vehicle vehicle);

    boolean leave(String slotId);

    Map<String, Vehicle> getAllOccupiedSlots();

    int countAvailableSlots();

    List<String> getRegistrationNumbersByColor(String color);

    List<String> getSlotsByColor(String color);

    String getSlotNumberByRegistrationNumber(String registrationNumber);

    void clearParkingLot();
}
