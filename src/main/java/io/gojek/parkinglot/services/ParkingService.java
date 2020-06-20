package io.gojek.parkinglot.services;

import io.gojek.parkinglot.objects.Vehicle;

import java.util.List;

public interface ParkingService {

    void createParkingLot(int capacity);

    int park(Vehicle vehicle);

    boolean leave(int slotNo);

    List<String> getStatus();

    int countAvailableSlots();

    List<String> getRegistrationNumbersByColor(String color);

    List<Integer> getSlotsByColor(String color);

    int getSlotNumberByRegistrationNumber(String registrationNumber);

    void clearParkingLot();
}
