package io.gojek.parkinglot.objects;


import io.gojek.parkinglot.objects.strategy.ParkingSlotStrategy;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ParkingLotTest {

    @Test
    public void should_createNewInstance_when_noInstance() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        assertThat(parkingLot).isNotNull();
        assertThat(parkingLot.getAvailableSlot()).isEqualTo(3);
        parkingLot.clear();
    }

    @Test
    public void should_getInstance_when_instanceAlreadyExist() {
        ParkingLot<Car> firstParkingLot = ParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        ParkingLot<Car> secondParkingLot = ParkingLot.getInstance(4, ParkingSlotStrategy.NEAREST_SLOT);
        assertThat(firstParkingLot).isNotNull();
        assertThat(secondParkingLot).isNotNull();
        assertThat(secondParkingLot).isEqualTo(firstParkingLot);
        assertThat(secondParkingLot.getAvailableSlot()).isEqualTo(3);
        secondParkingLot.clear();
    }

    @Test
    public void should_parkVehicle_when_slotsAvailable() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        int slotNo = parkingLot.park(new Car("KA-01-HH-1234", "White"));
        assertThat(slotNo).isEqualTo(1);
        assertThat(parkingLot.getAvailableSlot()).isEqualTo(2);
        parkingLot.clear();
    }

    @Test
    public void should_returnNoSpaceAvailable_when_noSlotsAreAvailable() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(1, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        int slotNo = parkingLot.park(new Car("KA-01-HH-1239", "White"));
        assertThat(slotNo).isEqualTo(Constants.NOT_AVAILABLE);
        assertThat(parkingLot.getAvailableSlot()).isEqualTo(0);
        parkingLot.clear();
    }

    @Test
    public void should_doNothing_when_vehicleAlreadyParked() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(1, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        int slotNo = parkingLot.park(new Car("KA-01-HH-1234", "White"));
        assertThat(slotNo).isEqualTo(Constants.ALREADY_EXIST);
        assertThat(parkingLot.getAvailableSlot()).isEqualTo(0);
        parkingLot.clear();
    }

    @Test
    public void should_leaveVehicle_when_slotOccupied() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(1, ParkingSlotStrategy.NEAREST_SLOT);
        int slotNo = parkingLot.park(new Car("KA-01-HH-1234", "White"));
        assertThat(parkingLot.leave(slotNo)).isTrue();
        parkingLot.clear();
    }

    @Test
    public void should_doNothing_when_slotIsEmpty() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(1, ParkingSlotStrategy.NEAREST_SLOT);
        assertThat(parkingLot.leave(1)).isFalse();
        parkingLot.clear();
    }

    @Test
    public void should_getAllOccupiedVehicles_when_thereAreVehicles() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        Map<Integer, Vehicle> occupiedSlots = parkingLot.getAllOccupiedSlots();
        assertThat(occupiedSlots).hasSize(2);
        parkingLot.clear();
    }

    @Test
    public void should_getAllOccupiedVehicles_when_thereAreNoVehicles() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        Map<Integer, Vehicle> occupiedSlots = parkingLot.getAllOccupiedSlots();
        assertThat(occupiedSlots).hasSize(0);
        parkingLot.clear();
    }

    @Test
    public void should_getSlotsByColor_when_slotsPresent() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        List<Integer> slots = parkingLot.getSlotNumbersByVehicleColor("White");
        assertThat(slots).hasSize(2);
        parkingLot.clear();
    }

    @Test
    public void should_getSlotsByColor_when_slotsAreNotPresent() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        List<Integer> slots = parkingLot.getSlotNumbersByVehicleColor("Red");
        assertThat(slots).hasSize(0);
        parkingLot.clear();
    }

    @Test
    public void should_getRegistrationByColor_when_vehiclesPresent() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        List<String> registrationNos = parkingLot.getRegistrationNumbersByVehicleColor("White");
        assertThat(registrationNos).hasSize(2);
        parkingLot.clear();
    }

    @Test
    public void should_getRegistrationByColor_when_vehiclesNotPresent() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        List<String> registrationNos = parkingLot.getRegistrationNumbersByVehicleColor("Red");
        assertThat(registrationNos).hasSize(0);
        parkingLot.clear();
    }

    @Test
    public void should_getSlotByRegistrationNo_when_vehiclesPresent() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        int slot = parkingLot.getSlotNumberByRegistrationNumber("KA-01-HH-1234");
        assertThat(slot).isEqualTo(1);
        parkingLot.clear();
    }

    @Test
    public void should_getSlotByRegistrationNo_when_vehiclesNotPresent() {
        ParkingLot<Car> parkingLot = ParkingLot.getInstance(3, ParkingSlotStrategy.NEAREST_SLOT);
        parkingLot.park(new Car("KA-01-HH-1234", "White"));
        parkingLot.park(new Car("KA-01-HH-1239", "White"));
        int slot = parkingLot.getSlotNumberByRegistrationNumber("KA-01-HH-1235");
        assertThat(slot).isEqualTo(Constants.NOT_FOUND);
        parkingLot.clear();
    }
}