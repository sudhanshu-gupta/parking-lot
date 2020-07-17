package io.gojek.parkinglot.model;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ParkingLot<T extends Vehicle> extends Serializable {

    String park(T vehicle);
    boolean leave(String slotId);
    Map<Integer, Vehicle> getAllOccupiedSlots();
    int getAvailableSlot();
    List<Integer> getSlotNumbersByVehicleColor(String color);
    List<String> getRegistrationNumbersByVehicleColor(String color);
    String getSlotNumberByRegistrationNumber(String registrationNumber);
    void clear();
}
