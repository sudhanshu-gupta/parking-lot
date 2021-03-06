package io.gojek.parkinglot.services.impl;


import io.gojek.parkinglot.model.Car;
import io.gojek.parkinglot.services.ParkingService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.assertj.core.api.Assertions.assertThat;

public class ParkingServiceImplTest {

    private ParkingService parkingService;

    @Before
    public void setUp() {
        parkingService = new ParkingServiceImpl(new ReentrantReadWriteLock());
    }

    @After
    public void tearDown() {
        parkingService.clearParkingLot();
    }

    @Test
    public void should_createParkingLot_when_instanceNotCreated() {
        parkingService.createParkingLot(3);
        assertThat(parkingService.countAvailableSlots()).isEqualTo(3);
    }

    @Test
    public void should_parkCar_when_slotAvailable() {
        parkingService.createParkingLot(3);
        String slotNo = parkingService.park(new Car("KA-01-HH-1234", "White"));
        assertThat(slotNo).isEqualTo(String.valueOf(1));
    }

    @Test
    public void should_leaveCar_when_slotOccupied() {
        parkingService.createParkingLot(3);
        parkingService.park(new Car("KA-01-HH-1234", "White"));
        parkingService.leave(String.valueOf(1));
        assertThat(parkingService.countAvailableSlots()).isEqualTo(3);
    }

    @Test
    public void should_getSlot_when_validRegistrationNo() {
        parkingService.createParkingLot(3);
        parkingService.park(new Car("KA-01-HH-1234", "White"));
        String slotNo = parkingService.getSlotNumberByRegistrationNumber("KA-01-HH-1234");
        assertThat(slotNo).isEqualTo(String.valueOf(1));
    }

    @Test
    public void should_getRegistrationNos_when_carsByColorPresent() {
        parkingService.createParkingLot(3);
        parkingService.park(new Car("KA-01-HH-1234", "White"));
        List<String> registrationNumbers = parkingService.getRegistrationNumbersByColor("White");
        assertThat(registrationNumbers).hasSize(1);
    }

    @Test
    public void should_getSlotNos_when_carsByColorPresent() {
        parkingService.createParkingLot(3);
        parkingService.park(new Car("KA-01-HH-1234", "White"));
        List<String> registrationNumbers = parkingService.getSlotsByColor("White");
        assertThat(registrationNumbers).hasSize(1);
    }
}