package io.gojek.parkinglot.model.strategy;

import io.gojek.parkinglot.model.Constants;

import java.util.SortedSet;
import java.util.TreeSet;

public class NearestParkingSlotStrategy implements ParkingSlotStrategy {

    private final SortedSet<Integer> freeSlots = new TreeSet<>();

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
