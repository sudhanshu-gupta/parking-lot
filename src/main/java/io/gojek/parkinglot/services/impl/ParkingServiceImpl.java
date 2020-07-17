package io.gojek.parkinglot.services.impl;

import io.gojek.parkinglot.model.OneLevelParkingLot;
import io.gojek.parkinglot.model.ParkingLot;
import io.gojek.parkinglot.model.Vehicle;
import io.gojek.parkinglot.model.strategy.ParkingSlotStrategy;
import io.gojek.parkinglot.services.ParkingService;
import io.gojek.parkinglot.utils.Assert;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final ReadWriteLock lock;
    private ParkingLot<Vehicle> parkingLot;

    /**
     * Creates the instance of parking Lot if instance is already null. If instance already
     * exist, it throws the IllegalStateException.java exception.
     * @param capacity valid capacity of the parking Lot
     */
    @Override
    public void createParkingLot(int capacity) {
        if (parkingLot != null) {
            throw new IllegalStateException("Parking lot already exist");
        }
        Assert.greaterThanOrEqualTo(capacity, 1, "Invalid capacity");
        parkingLot = OneLevelParkingLot.getInstance(capacity, ParkingSlotStrategy.NEAREST_SLOT);
    }

    /**
     * Park a vehicle in the slot selected by the slot assignment strategy class. If no slots available, returns -1, if
     * vehicle already parked, return -2
     * @param vehicle vehicle instance with valid registration number and color
     * @return assigned slot number if slot is available else return -1
     */
    @Override
    public String park(Vehicle vehicle) {
        validateParkingLot();
        lock.writeLock().lock();
        try {
            return parkingLot.park(vehicle);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * unpark a vehicle from the desired slot, if slot number is valid and vehicle is present
     * @param slotId slot number where the vehicle is parked
     * @return true, if vehicle is removed from the slot, else false
     */
    @Override
    public boolean leave(String slotId) {
        validateParkingLot();
        lock.writeLock().lock();
        try {
            return parkingLot.leave(slotId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * fetch all occupied slots with vehicles
     * @return return all the slots which are currently occupied along with its parked vehicle instances
     */
    @Override
    public Map<String, Vehicle> getAllOccupiedSlots() {
        validateParkingLot();
        lock.readLock().lock();
        try {
            return parkingLot.getAllOccupiedSlots();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * @return get the total number of available slot count
     */
    @Override
    public int countAvailableSlots() {
        validateParkingLot();
        lock.readLock().lock();
        try {
            return parkingLot.getAvailableSlot();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * fetch all the registration number of the vehicles which has the given color. If no such vehicle is present, it
     * return the empty list.
     * @param color color given valid color. input case is ignore while doing comparision.
     * @return ist of vehicle registration number with given colored vehicle.
     */
    @Override
    public List<String> getRegistrationNumbersByColor(String color) {
        validateParkingLot();
        lock.readLock().lock();
        try {
            return parkingLot.getRegistrationNumbersByVehicleColor(color);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * fetch the all the occupied slots which has the vehicle of the given color. If no such slot is present, return the
     * empty list of slots.
     * @param color given valid color. input case is ignore while doing comparision.
     * @return list of all occupied slots with given colored vehicle parked.
     */
    @Override
    public List<String> getSlotsByColor(String color) {
        validateParkingLot();
        lock.readLock().lock();
        try {
            return parkingLot.getSlotNumbersByVehicleColor(color);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * get the slot number of the vehicle with the given registration number where the vehicle is parked. If no such
     * vehicle is present, it return the -1 (Not Found)
     * @param registrationNumber valid registration number of the vehicle
     * @return valid slot number if vehicle with given registration number is present, else -1 (Not Found)
     */
    @Override
    public String getSlotNumberByRegistrationNumber(String registrationNumber) {
        validateParkingLot();
        lock.readLock().lock();
        try {
            return parkingLot.getSlotNumberByRegistrationNumber(registrationNumber);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * clear the parking Lot data structure. It resent all the variables to default values. It also, assign null to
     * the Parking Lot instance
     */
    @Override
    public void clearParkingLot() {
        validateParkingLot();
        lock.writeLock().lock();
        try {
            parkingLot.clear();
            parkingLot = null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void validateParkingLot() {
        if (parkingLot == null) {
            throw new IllegalStateException("Parking lot does not exist");
        }
    }
}
