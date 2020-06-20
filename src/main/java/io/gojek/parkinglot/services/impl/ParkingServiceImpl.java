package io.gojek.parkinglot.services.impl;

import io.gojek.parkinglot.objects.ParkingLot;
import io.gojek.parkinglot.objects.Vehicle;
import io.gojek.parkinglot.objects.strategy.ParkingSlotStrategy;
import io.gojek.parkinglot.services.ParkingService;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ParkingServiceImpl implements ParkingService {

    private ParkingLot<Vehicle> parkingLot;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    @Override
    public void createParkingLot(int capacity) {
        if (parkingLot != null) {
            throw new IllegalStateException("Parking lot already exist");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Invalid capacity");
        }
        parkingLot = ParkingLot.getInstance(capacity, ParkingSlotStrategy.NEAREST_SLOT);
    }

    @Override
    public int park(Vehicle vehicle) {
        validateParkingLot();
        lock.writeLock().lock();
        try {
            return parkingLot.park(vehicle);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean leave(int slotNo) {
        validateParkingLot();
        lock.writeLock().lock();
        try {
            return parkingLot.leave(slotNo);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<String> getStatus() {
        validateParkingLot();
        lock.readLock().lock();
        try {
            return parkingLot.status();
        } finally {
            lock.readLock().unlock();
        }
    }

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

    @Override
    public List<Integer> getSlotsByColor(String color) {
        validateParkingLot();
        lock.readLock().lock();
        try {
            return parkingLot.getSlotNumbersByVehicleColor(color);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int getSlotNumberByRegistrationNumber(String registrationNumber) {
        validateParkingLot();
        lock.readLock().lock();
        try {
            return parkingLot.getSlotNumberByRegistrationNumber(registrationNumber);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void clearParkingLot() {
        validateParkingLot();
        lock.writeLock().lock();
        try {
            parkingLot.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void validateParkingLot() {
        if(parkingLot == null) {
            throw new IllegalStateException("Parking lot does not exist");
        }
    }
}
