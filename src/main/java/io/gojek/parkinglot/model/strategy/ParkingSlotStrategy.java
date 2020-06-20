package io.gojek.parkinglot.model.strategy;

public interface ParkingSlotStrategy {

    ParkingSlotStrategy NEAREST_SLOT = new NearestParkingSlotStrategy();

    void addSlot(int slotNo);

    void removeSlot(int slotNo);

    int getSlot();
}
