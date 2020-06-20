package io.gojek.parkinglot.objects;

import io.gojek.parkinglot.objects.strategy.ParkingSlotStrategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class ParkingLot<T extends Vehicle> {

    private AtomicInteger capacity = new AtomicInteger();
    private AtomicInteger available = new AtomicInteger();
    private AtomicReference<ParkingSlotStrategy> parkingSlotStrategy = new AtomicReference<>();
    private Map<Integer, Optional<T>> slots;
    private static final String STATUS_PLACEHOLDER = "%d\t\t%s\t\t%s";

    private static ParkingLot instance;

    @SuppressWarnings("unchecked")
    public static <T extends Vehicle> ParkingLot<T> getInstance(int capacity, ParkingSlotStrategy parkingSlotStrategy) {
        if(instance == null) {
            synchronized (ParkingLot.class) {
                if(instance == null) {
                    instance = new ParkingLot<T>(capacity, parkingSlotStrategy);
                }
            }
        }
        return instance;
    }

    private ParkingLot(int capacity, ParkingSlotStrategy parkingSlotStrategy) {
        this.capacity.set(capacity);
        this.available.set(capacity);
        this.parkingSlotStrategy.set(parkingSlotStrategy);
        this.slots  = new ConcurrentHashMap<>();
        for(int slot=1; slot <= capacity; slot++) {
            this.parkingSlotStrategy.get().addSlot(slot);
            this.slots.put(slot, Optional.empty());
        }
    }

    public int park(T vehicle) {
        if(available.get() == 0) {
            return Constants.NOT_AVAILABLE;
        }
        if(slots.containsValue(Optional.of(vehicle))) {
            return Constants.ALREADY_EXIST;
        }
        int availableSlot = parkingSlotStrategy.get().getSlot();
        slots.put(availableSlot, Optional.of(vehicle));
        available.decrementAndGet();
        parkingSlotStrategy.get().removeSlot(availableSlot);
        return availableSlot;
    }

    public boolean leave(int slotNo) {
        if(slotNo < 0 || slotNo > capacity.get() || !slots.get(slotNo).isPresent()) {
            return false;
        }
        available.incrementAndGet();
        parkingSlotStrategy.get().addSlot(slotNo);
        slots.put(slotNo, Optional.empty());
        return true;
    }

    public List<String> status() {
        return slots.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent())
                .map(entry -> String.format(STATUS_PLACEHOLDER, entry.getKey(),
                        entry.getValue().get().getRegistrationNo(), entry.getValue().get().getColor()))
                .collect(Collectors.toList());
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
        return slots.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent() && entry.getValue().get().getColor().equalsIgnoreCase(color))
                .map(entry -> entry.getValue().get().getRegistrationNo())
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
        instance = null;
    }
}
