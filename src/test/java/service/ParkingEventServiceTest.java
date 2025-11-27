package service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.ParkingEventNotFoundException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.ParkingEventRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.ParkingEventService;
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

    private final Double DYNAMIC_PRICE_MULTIPLIER = 1.0;
    private final OffsetDateTime ENTRY_TIME = OffsetDateTime.now().minusHours(1);
    private final Long ID = 1L;
    private final double LAT = -23.5505;
    private final double LNG = -46.6333;
    private final Boolean IS_OCCUPIED = false;
    private final String LICENSE_PLATE = "ABC-1234";

    private final String SECTOR = "A1";
    private final Double BASE_PRICE = 10.0;
    private final int MAX_CAPACITY = 100;
    private final Integer CURRENT_OCCUPANCY = 50;
    private final LocalTime OPEN_HOUR = LocalTime.of(8, 0);
    private final LocalTime CLOSE_HOUR = LocalTime.of(20, 0);


    @BeforeEach
    void setUp() {

        mockGarage = new GarageModel(SECTOR, BASE_PRICE, MAX_CAPACITY, CURRENT_OCCUPANCY, OPEN_HOUR, CLOSE_HOUR);
        spot = new SpotModel(ID, LAT, LNG, mockGarage, IS_OCCUPIED, LICENSE_PLATE);


        activeParkingEvent = new ParkingEventModel(
                ID,
                LICENSE_PLATE,
                ENTRY_TIME,
                null,
                null,
                0.0,
                0.0,
                DYNAMIC_PRICE_MULTIPLIER,
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
        assertEquals(ID, activeParkingEvent.getId());
        assertEquals(LICENSE_PLATE, activeParkingEvent.getLicensePlate());
        assertEquals(ENTRY_TIME, activeParkingEvent.getEntryTime());
        assertEquals(DYNAMIC_PRICE_MULTIPLIER, activeParkingEvent.getDynamicPriceMultiplier());
        assertEquals(SECTOR, activeParkingEvent.getGarageModel().getSector());
        assertEquals(ID, activeParkingEvent.getSpotModel().getId());

        verify(repository, times(1)).save(activeParkingEvent);
    }


    @Test
    void findParkingEventByLicensePlate_WhenActiveEventExists_ShouldReturnParkingEventModelObject() {

        when(repository.findByLicensePlateAndExitTimeIsNull(LICENSE_PLATE))
                .thenReturn(Optional.of(activeParkingEvent));


        ParkingEventModel foundEvent = parkingEventService.findParkingEventByLicensePlate(LICENSE_PLATE);


        assertNotNull(foundEvent);
        assertNull(foundEvent.getExitTime());
        verify(repository, times(1)).findByLicensePlateAndExitTimeIsNull(LICENSE_PLATE);
    }

    @Test
    void findParkingEventByLicensePlate_WhenNoActiveParkingEventExists_ShouldThrowException() {

        when(repository.findByLicensePlateAndExitTimeIsNull(LICENSE_PLATE))
                .thenReturn(Optional.empty());


        ParkingEventNotFoundException exception = assertThrows(ParkingEventNotFoundException.class, () -> {
            parkingEventService.findParkingEventByLicensePlate(LICENSE_PLATE);
        });


        assertNotNull(exception);
        assertEquals(exception.getMessage(), String.format(NOT_FOUND_MESSAGE, LICENSE_PLATE));
        verify(repository, times(1)).findByLicensePlateAndExitTimeIsNull(LICENSE_PLATE);
    }


    @Test
    void isVehicleCurrentlyParked_WhenActiveEventExists_ShouldReturnTrue() {

        when(repository.findByLicensePlateAndExitTimeIsNull(LICENSE_PLATE))
                .thenReturn(Optional.of(activeParkingEvent));


        assertTrue(parkingEventService.isVehicleCurrentlyParked(LICENSE_PLATE));
        verify(repository, times(1)).findByLicensePlateAndExitTimeIsNull(LICENSE_PLATE);
    }

    @Test
    void isVehicleCurrentlyParked_WhenNoActiveEventExists_ShouldReturnFalse() {

        when(repository.findByLicensePlateAndExitTimeIsNull(LICENSE_PLATE))
                .thenReturn(Optional.empty());


        assertFalse(parkingEventService.isVehicleCurrentlyParked(LICENSE_PLATE));
        verify(repository, times(1)).findByLicensePlateAndExitTimeIsNull(LICENSE_PLATE);
    }
}