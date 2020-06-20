package io.gojek.parkinglot.services;

import io.gojek.parkinglot.model.Vehicle;

import java.util.List;
import java.util.Map;

public interface ParkingService {

    void createParkingLot(int capacity);

    int park(Vehicle vehicle);

    boolean leave(int slotNo);

    Map<Integer, Vehicle> getAllOccupiedSlots();

    int countAvailableSlots();

    List<String> getRegistrationNumbersByColor(String color);

    List<Integer> getSlotsByColor(String color);

    int getSlotNumberByRegistrationNumber(String registrationNumber);

    void clearParkingLot();
}
