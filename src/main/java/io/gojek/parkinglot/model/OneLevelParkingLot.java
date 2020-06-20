package io.gojek.parkinglot.model;

import io.gojek.parkinglot.model.strategy.ParkingSlotStrategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class OneLevelParkingLot<T extends Vehicle> implements ParkingLot<T> {

    @SuppressWarnings("rawtypes")
    private static OneLevelParkingLot instance;
    private AtomicInteger capacity = new AtomicInteger();
    private AtomicInteger available = new AtomicInteger();
    private AtomicReference<ParkingSlotStrategy> parkingSlotStrategy = new AtomicReference<>();
    private AtomicReference<Set<String>> registrationNumbers = new AtomicReference<>();
    private Map<Integer, Optional<T>> slots;

    private OneLevelParkingLot(int capacity, ParkingSlotStrategy parkingSlotStrategy) {
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

    /**
     * Returns the singleton instance of the Parking Lot class. Singleton instance is provided to avoid to have multiple
     * instances of the lot in the same eco system.
     * @param capacity initial slot capacity of the parking lot
     * @param parkingSlotStrategy strategy to find a empty slot when multiple options are available
     * @param <T> vehicle type
     * @return singleton instance of the parking lot. If instance already exist, it return the same. else it creates a
     * new instance of the @ParkingLot.java
     */
    @SuppressWarnings("unchecked")
    public static <T extends Vehicle> ParkingLot<T> getInstance(int capacity, ParkingSlotStrategy parkingSlotStrategy) {
        if (instance == null) {
            synchronized (OneLevelParkingLot.class) {
                if (instance == null) {
                    instance = new OneLevelParkingLot<T>(capacity, parkingSlotStrategy);
                }
            }
        }
        return instance;
    }

    /**
     * Park a vehicle in the slot selected by the slot assignment strategy class. If no slots available, returns -1, if
     * vehicle already parked, return -2
     * @param vehicle vehicle instance with valid registration number and color
     * @return assigned slot number if slot is available else return -1
     */
    @Override
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

    /**
     * unpark a vehicle from the desired slot, if slot number is valid and vehicle is present
     * @param slotNo slot number where the vehicle is parked
     * @return true, if vehicle is removed from the slot, else false
     */
    @Override
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

    /**
     * fetch all occupied slots with vehicles
     * @return return all the slots which are currently occupied along with its parked vehicle instances
     */
    @Override
    public Map<Integer, Vehicle> getAllOccupiedSlots() {
        return slots.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, a -> a.getValue().get()));
    }

    /**
     * @return get the total number of available slot count
     */
    @Override
    public int getAvailableSlot() {
        return available.get();
    }

    /**
     * fetch the all the occupied slots which has the vehicle of the given color. If no such slot is present, return the
     * empty list of slots.
     * @param color given valid color. input case is ignore while doing comparision.
     * @return list of all occupied slots with given colored vehicle parked.
     */
    @Override
    public List<Integer> getSlotNumbersByVehicleColor(String color) {
        return slots.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent() && entry.getValue().get().getColor().equalsIgnoreCase(color))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * fetch all the registration number of the vehicles which has the given color. If no such vehicle is present, it
     * return the empty list.
     * @param color color given valid color. input case is ignore while doing comparision.
     * @return ist of vehicle registration number with given colored vehicle.
     */
    @Override
    public List<String> getRegistrationNumbersByVehicleColor(String color) {
        return slots.values().stream()
                .filter(t -> t.isPresent() && t.get().getColor().equalsIgnoreCase(color))
                .map(t -> t.get().getRegistrationNo())
                .collect(Collectors.toList());
    }

    /**
     * get the slot number of the vehicle with the given registration number where the vehicle is parked. If no such
     * vehicle is present, it return the -1 (Not Found)
     * @param registrationNumber valid registration number of the vehicle
     * @return valid slot number if vehicle with given registration number is present, else -1 (Not Found)
     */
    @Override
    public int getSlotNumberByRegistrationNumber(String registrationNumber) {
        return slots.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent() && entry.getValue().get().getRegistrationNo().equalsIgnoreCase(registrationNumber))
                .mapToInt(Map.Entry::getKey)
                .findFirst().orElse(Constants.NOT_FOUND);
    }

    /**
     * clear the parking Lot data structure. It resent all the variables to default values. It also, assign null to
     * the Parking Lot instance
     */
    @Override
    public void clear() {
        available = new AtomicInteger();
        parkingSlotStrategy = new AtomicReference<>();
        capacity = new AtomicInteger();
        slots = new ConcurrentHashMap<>();
        registrationNumbers = new AtomicReference<>();
        instance = null;
    }
}
