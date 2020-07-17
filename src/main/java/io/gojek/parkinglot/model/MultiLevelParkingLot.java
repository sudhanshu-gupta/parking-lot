package io.gojek.parkinglot.model;

import java.util.List;
import java.util.Map;

public class MultiLevelParkingLot implements ParkingLot<Vehicle> {

    @Override
    public String park(Vehicle vehicle) {
        return null;
    }

    @Override
    public boolean leave(String slotId) {
        return false;
    }

    @Override
    public Map<Integer, Vehicle> getAllOccupiedSlots() {
        return null;
    }

    @Override
    public int getAvailableSlot() {
        return 0;
    }

    @Override
    public List<Integer> getSlotNumbersByVehicleColor(String color) {
        return null;
    }

    @Override
    public List<String> getRegistrationNumbersByVehicleColor(String color) {
        return null;
    }

    @Override
    public String getSlotNumberByRegistrationNumber(String registrationNumber) {
        return null;
    }

    @Override
    public void clear() {

    }
}
