package io.gojek.parkinglot.objects;

import io.gojek.parkinglot.objects.strategy.ParkingSlotStrategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class ParkingLot<T extends Vehicle> {

    @SuppressWarnings("rawtypes")
    private static ParkingLot instance;
    private AtomicInteger capacity = new AtomicInteger();
    private AtomicInteger available = new AtomicInteger();
    private AtomicReference<ParkingSlotStrategy> parkingSlotStrategy = new AtomicReference<>();
    private AtomicReference<Set<String>> registrationNumbers = new AtomicReference<>();
    private Map<Integer, Optional<T>> slots;

    private ParkingLot(int capacity, ParkingSlotStrategy parkingSlotStrategy) {
        this.capacity.set(capacity);
        this.available.set(capacity);
        this.parkingSlotStrategy.set(parkingSlotStrategy);
        this.registrationNumbers.set(new HashSet<>());
        this.slots = new ConcurrentHashMap<>();
        for (int slot = 1; slot <= capacity; slot++) {
            this.parkingSlotStrategy.get().addSlot(slot);
            this.slots.put(slot, Optional.empty());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Vehicle> ParkingLot<T> getInstance(int capacity, ParkingSlotStrategy parkingSlotStrategy) {
        if (instance == null) {
            synchronized (ParkingLot.class) {
                if (instance == null) {
                    instance = new ParkingLot<T>(capacity, parkingSlotStrategy);
                }
            }
        }
        return instance;
    }

    public int park(T vehicle) {
        if (registrationNumbers.get().contains(vehicle.getRegistrationNo())) {
            return Constants.ALREADY_EXIST;
        } else if (available.get() == 0) {
            return Constants.NOT_AVAILABLE;
        }
        int availableSlot = parkingSlotStrategy.get().getSlot();
        slots.put(availableSlot, Optional.of(vehicle));
        available.decrementAndGet();
        registrationNumbers.get().add(vehicle.getRegistrationNo());
        parkingSlotStrategy.get().removeSlot(availableSlot);
        return availableSlot;
    }

    public boolean leave(int slotNo) {
        if (slotNo < 0 || slotNo > capacity.get() || !slots.get(slotNo).isPresent()) {
            return false;
        }
        available.incrementAndGet();
        registrationNumbers.get().remove(slots.get(slotNo).get().getRegistrationNo());
        parkingSlotStrategy.get().addSlot(slotNo);
        slots.put(slotNo, Optional.empty());
        return true;
    }

    public Map<Integer, Vehicle> getAllOccupiedSlots() {
        return slots.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, a -> a.getValue().get()));
    }

    public int getAvailableSlot() {
        return available.get();
    }

    public List<Integer> getSlotNumbersByVehicleColor(String color) {
        return slots.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent() && entry.getValue().get().getColor().equalsIgnoreCase(color))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<String> getRegistrationNumbersByVehicleColor(String color) {
        return slots.values().stream()
                .filter(t -> t.isPresent() && t.get().getColor().equalsIgnoreCase(color))
                .map(t -> t.get().getRegistrationNo())
                .collect(Collectors.toList());
    }

    public int getSlotNumberByRegistrationNumber(String registrationNumber) {
        return slots.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent() && entry.getValue().get().getRegistrationNo().equalsIgnoreCase(registrationNumber))
                .mapToInt(Map.Entry::getKey)
                .findFirst().orElse(Constants.NOT_FOUND);
    }

    public void clear() {
        available = new AtomicInteger();
        parkingSlotStrategy = new AtomicReference<>();
        capacity = new AtomicInteger();
        slots = new ConcurrentHashMap<>();
        registrationNumbers = new AtomicReference<>();
        instance = null;
    }
}
