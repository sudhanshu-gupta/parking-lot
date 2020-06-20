package io.gojek.parkinglot.objects.strategy;

public interface ParkingSlotStrategy {

    void addSlot(int slotNo);

    void removeSlot(int slotNo);

    int getSlot();

    ParkingSlotStrategy NEAREST_SLOT = new NearestParkingSlotStrategy();
}
