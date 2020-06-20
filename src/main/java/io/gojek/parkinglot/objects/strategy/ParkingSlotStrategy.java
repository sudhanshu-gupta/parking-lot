package io.gojek.parkinglot.objects.strategy;

public interface ParkingSlotStrategy {

    ParkingSlotStrategy NEAREST_SLOT = new NearestParkingSlotStrategy();

    void addSlot(int slotNo);

    void removeSlot(int slotNo);

    int getSlot();
}
