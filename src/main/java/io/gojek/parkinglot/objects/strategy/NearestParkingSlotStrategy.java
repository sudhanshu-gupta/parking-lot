package io.gojek.parkinglot.objects.strategy;

import io.gojek.parkinglot.objects.Constants;

import java.util.TreeSet;

public class NearestParkingSlotStrategy implements ParkingSlotStrategy {

    private final TreeSet<Integer> freeSlots = new TreeSet<>();

    @Override
    public void addSlot(int slotNo) {
        freeSlots.add(slotNo);
    }

    @Override
    public void removeSlot(int slotNo) {
        freeSlots.remove(slotNo);
    }

    @Override
    public int getSlot() {
        if (freeSlots.size() == 0) return Constants.NOT_AVAILABLE;
        return freeSlots.first();
    }
}
