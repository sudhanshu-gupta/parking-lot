package io.gojek.parkinglot.model;


import io.gojek.parkinglot.model.strategy.ParkingSlotStrategy;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OneLevelParkingLotTest {

    @Test
    public void should_createNewInstance_when_noInstance() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        assertThat(parkingLot).isNotNull();
        assertThat(parkingLot.getAvailableSlot()).isEqualTo(3);
        parkingLot.clear();
    }

    @Test
    public void should_getInstance_when_instanceAlreadyExist() {
        ParkingLot<Car> firstParkingLot = OneLevelParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        ParkingLot<Car> secondParkingLot = OneLevelParkingLot.getInstance(4, ParkingSlotStrategy.NEAREST_SLOT);
        assertThat(firstParkingLot).isNotNull();
        assertThat(secondParkingLot).isNotNull();
        assertThat(secondParkingLot).isEqualTo(firstParkingLot);
        assertThat(secondParkingLot.getAvailableSlot()).isEqualTo(3);
        secondParkingLot.clear();
    }

    @Test
    public void should_parkVehicle_when_slotsAvailable() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        String slotNo = parkingLot.park(new Car("KA-01-HH-1234", "White"));
        assertThat(slotNo).isEqualTo(String.valueOf(1));
        assertThat(parkingLot.getAvailableSlot()).isEqualTo(2);
        parkingLot.clear();
    }

    @Test
    public void should_returnNoSpaceAvailable_when_noSlotsAreAvailable() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(1, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        String slotNo = parkingLot.park(new Car("KA-01-HH-1239", "White"));
        assertThat(slotNo).isEqualTo(String.valueOf(Constants.NOT_AVAILABLE));
        assertThat(parkingLot.getAvailableSlot()).isEqualTo(0);
        parkingLot.clear();
    }

    @Test
    public void should_doNothing_when_vehicleAlreadyParked() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(1, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        String slotNo = parkingLot.park(new Car("KA-01-HH-1234", "White"));
        assertThat(slotNo).isEqualTo(String.valueOf(Constants.ALREADY_EXIST));
        assertThat(parkingLot.getAvailableSlot()).isEqualTo(0);
        parkingLot.clear();
    }

    @Test
    public void should_leaveVehicle_when_slotOccupied() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(1, ParkingSlotStrategy.NEAREST_SLOT);
        String slotNo = parkingLot.park(new Car("KA-01-HH-1234", "White"));
        assertThat(parkingLot.leave(slotNo)).isTrue();
        parkingLot.clear();
    }

    @Test
    public void should_doNothing_when_slotIsEmpty() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(1, ParkingSlotStrategy.NEAREST_SLOT);
        assertThat(parkingLot.leave(String.valueOf(1))).isFalse();
        parkingLot.clear();
    }

    @Test
    public void should_getAllOccupiedVehicles_when_thereAreVehicles() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        Map<String, Vehicle> occupiedSlots = parkingLot.getAllOccupiedSlots();
        assertThat(occupiedSlots).hasSize(2);
        parkingLot.clear();
    }

    @Test
    public void should_getAllOccupiedVehicles_when_thereAreNoVehicles() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        Map<String, Vehicle> occupiedSlots = parkingLot.getAllOccupiedSlots();
        assertThat(occupiedSlots).hasSize(0);
        parkingLot.clear();
    }

    @Test
    public void should_getSlotsByColor_when_slotsPresent() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        List<String> slots = parkingLot.getSlotNumbersByVehicleColor("White");
        assertThat(slots).hasSize(2);
        parkingLot.clear();
    }

    @Test
    public void should_getSlotsByColor_when_slotsAreNotPresent() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        List<String> slots = parkingLot.getSlotNumbersByVehicleColor("Red");
        assertThat(slots).hasSize(0);
        parkingLot.clear();
    }

    @Test
    public void should_getRegistrationByColor_when_vehiclesPresent() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        List<String> registrationNos = parkingLot.getRegistrationNumbersByVehicleColor("White");
        assertThat(registrationNos).hasSize(2);
        parkingLot.clear();
    }

    @Test
    public void should_getRegistrationByColor_when_vehiclesNotPresent() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        List<String> registrationNos = parkingLot.getRegistrationNumbersByVehicleColor("Red");
        assertThat(registrationNos).hasSize(0);
        parkingLot.clear();
    }

    @Test
    public void should_getSlotByRegistrationNo_when_vehiclesPresent() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        String slot = parkingLot.getSlotNumberByRegistrationNumber("KA-01-HH-1234");
        assertThat(slot).isEqualTo(String.valueOf(1));
        parkingLot.clear();
    }

    @Test
    public void should_getSlotByRegistrationNo_when_vehiclesNotPresent() {
        ParkingLot<Car> parkingLot = OneLevelParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        String slot = parkingLot.getSlotNumberByRegistrationNumber("KA-01-HH-1235");
        assertThat(slot).isEqualTo(String.valueOf(Constants.NOT_FOUND));
        parkingLot.clear();
    }
}