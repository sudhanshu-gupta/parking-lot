package io.gojek.parkinglot.model;


import java.util.List;
import java.util.Map;

public interface ParkingLot<T extends Vehicle> {

    int park(T vehicle);
    boolean leave(int slotNo);
    Map<Integer, Vehicle> getAllOccupiedSlots();
    int getAvailableSlot();
    List<Integer> getSlotNumbersByVehicleColor(String color);
    List<String> getRegistrationNumbersByVehicleColor(String color);
    int getSlotNumberByRegistrationNumber(String registrationNumber);
    void clear();
}
