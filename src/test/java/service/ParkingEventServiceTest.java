package service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.ParkingEventNotFoundException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.ParkingEventRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.ParkingEventService;
import constants.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingEventServiceTest {

    @Mock
    private ParkingEventRepository repository;

    @InjectMocks
    private ParkingEventService parkingEventService;


    private ParkingEventModel activeParkingEvent;

    private final String NOT_FOUND_MESSAGE = "A vehicle with that license plate %s was not found";

    private SpotModel spot;
    private GarageModel mockGarage;

    @BeforeEach
    void setUp() {

        mockGarage = new GarageModel(TestConstants.SECTOR, TestConstants.BASE_PRICE, TestConstants.MAX_CAPACITY, TestConstants.CURRENT_OCCUPANCY,
                TestConstants.OPEN_HOUR, TestConstants.CLOSE_HOUR);

        spot = new SpotModel(TestConstants.ID, TestConstants.LAT, TestConstants.LNG, mockGarage,
                TestConstants.IS_OCCUPIED_FALSE, TestConstants.LICENSE_PLATE);


        activeParkingEvent = new ParkingEventModel(
                TestConstants.ID,
                TestConstants.LICENSE_PLATE,
                TestConstants.ENTRY_TIME,
                null,
                null,
                0.0,
                0.0,
                TestConstants.DYNAMIC_PRICE_MULTIPLIER,
                spot,
                mockGarage
        );
    }


    @Test
    void saveParkingEvent_ShouldReturnSavedEvent() {

        when(repository.save(activeParkingEvent)).thenReturn(activeParkingEvent);


        ParkingEventModel savedEvent = parkingEventService.saveParkingEvent(activeParkingEvent);


        assertNotNull(savedEvent);
        assertNotNull(activeParkingEvent.getId());
        assertEquals(TestConstants.ID, activeParkingEvent.getId());
        assertEquals(TestConstants.LICENSE_PLATE, activeParkingEvent.getLicensePlate());
        assertEquals(TestConstants.ENTRY_TIME, activeParkingEvent.getEntryTime());
        assertEquals(TestConstants.DYNAMIC_PRICE_MULTIPLIER, activeParkingEvent.getDynamicPriceMultiplier());
        assertEquals(TestConstants.SECTOR, activeParkingEvent.getGarageModel().getSector());
        assertEquals(TestConstants.ID, activeParkingEvent.getSpotModel().getId());

        verify(repository, times(1)).save(activeParkingEvent);
    }


    @Test
    void findParkingEventByLicensePlate_WhenActiveEventExists_ShouldReturnParkingEventModelObject() {

        when(repository.findByLicensePlateAndExitTimeIsNull(TestConstants.LICENSE_PLATE))
                .thenReturn(Optional.of(activeParkingEvent));


        ParkingEventModel foundEvent = parkingEventService.findParkingEventByLicensePlate(TestConstants.LICENSE_PLATE);


        assertNotNull(foundEvent);
        assertNull(foundEvent.getExitTime());
        verify(repository, times(1)).findByLicensePlateAndExitTimeIsNull(TestConstants.LICENSE_PLATE);
    }

    @Test
    void findParkingEventByLicensePlate_WhenNoActiveParkingEventExists_ShouldThrowException() {

        when(repository.findByLicensePlateAndExitTimeIsNull(TestConstants.LICENSE_PLATE))
                .thenReturn(Optional.empty());


        ParkingEventNotFoundException exception = assertThrows(ParkingEventNotFoundException.class, () -> {
            parkingEventService.findParkingEventByLicensePlate(TestConstants.LICENSE_PLATE);
        });


        assertNotNull(exception);
        assertEquals(exception.getMessage(), String.format(NOT_FOUND_MESSAGE, TestConstants.LICENSE_PLATE));
        verify(repository, times(1)).findByLicensePlateAndExitTimeIsNull(TestConstants.LICENSE_PLATE);
    }


    @Test
    void isVehicleCurrentlyParked_WhenActiveEventExists_ShouldReturnTrue() {

        when(repository.findByLicensePlateAndExitTimeIsNull(TestConstants.LICENSE_PLATE))
                .thenReturn(Optional.of(activeParkingEvent));


        assertTrue(parkingEventService.isVehicleCurrentlyParked(TestConstants.LICENSE_PLATE));
        verify(repository, times(1)).findByLicensePlateAndExitTimeIsNull(TestConstants.LICENSE_PLATE);
    }

    @Test
    void isVehicleCurrentlyParked_WhenNoActiveEventExists_ShouldReturnFalse() {

        when(repository.findByLicensePlateAndExitTimeIsNull(TestConstants.LICENSE_PLATE))
                .thenReturn(Optional.empty());


        assertFalse(parkingEventService.isVehicleCurrentlyParked(TestConstants.LICENSE_PLATE));
        verify(repository, times(1)).findByLicensePlateAndExitTimeIsNull(TestConstants.LICENSE_PLATE);
    }
}