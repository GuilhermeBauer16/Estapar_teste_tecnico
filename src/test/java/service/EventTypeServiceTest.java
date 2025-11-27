package service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.ActiveParkingNotFoundException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.GarageClosedException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.GarageFullException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.VehicleAlreadyParkedException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.ParkingEventRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.EventTypeService;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.EventTypeValidationService;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.GarageService;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.ParkingEventService;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.SpotService;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.utils.PricingCalculator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventTypeServiceTest {

    @Mock
    private ParkingEventRepository parkingEventRepository;

    @Mock
    private GarageService garageService;

    @Mock
    private SpotService spotService;

    @Mock
    private ParkingEventService parkingEventService;

    @Mock
    private EventTypeValidationService eventValidationService;

    @InjectMocks
    private EventTypeService eventTypeService;

    private static final String SECTOR = "A";
    private static final String LICENSE = "ABC1234";
    private static final ZoneId GARAGE_ZONE = ZoneId.of("America/Sao_Paulo");
    private final Double DYNAMIC_PRICE_MULTIPLIER = 1.0;

    private GarageModel garage;
    private SpotModel spot;
    private ParkingEventModel parkingEvent;

    private MockedStatic<PricingCalculator> pricingMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        garage = new GarageModel();
        garage.setMaxCapacity(10);
        garage.setCurrentOccupancy(5);
        garage.setOpenHour(LocalTime.of(6, 0));
        garage.setCloseHour(LocalTime.of(23, 0));
        garage.setBasePrice(10.0);

        spot = new SpotModel();
        spot.setOccupied(false);
        spot.setGarageModel(garage);

        parkingEvent = new ParkingEventModel();
        parkingEvent.setLicensePlate(LICENSE);
        parkingEvent.setEntryTime(OffsetDateTime.now(GARAGE_ZONE).minusHours(1));
        parkingEvent.setGarageModel(garage);
        parkingEvent.setSpotModel(spot);
        parkingEvent.setDynamicPriceMultiplier(DYNAMIC_PRICE_MULTIPLIER);

        pricingMock = mockStatic(PricingCalculator.class);
    }

    @AfterEach
    void tearDown() {
        pricingMock.close();
    }


    @Test
    void handleEntryEvent_shouldCreateEventSuccessfully() {

        when(parkingEventService.isVehicleCurrentlyParked(LICENSE)).thenReturn(false);
        when(spotService.findFistAvailableSpot(SECTOR)).thenReturn(spot);
        when(garageService.isGarageOpen(any(), any(), any())).thenReturn(true);
        when(spotService.saveSpot(any())).thenReturn(spot);
        when(garageService.saveGarage(any())).thenReturn(garage);
        when(parkingEventService.saveParkingEvent(any())).thenReturn(parkingEvent);

        pricingMock.when(() -> PricingCalculator.calculateDynamicMultiplier(garage))
                .thenReturn(1.5);

        OffsetDateTime entry = OffsetDateTime.now(GARAGE_ZONE);

        ParkingEventModel result = eventTypeService.handleEntryEvent(SECTOR, LICENSE, entry);

        assertNotNull(result);
        assertEquals(LICENSE, result.getLicensePlate());


        verify(spotService).saveSpot(any());
        verify(garageService).saveGarage(any());
        verify(parkingEventService).saveParkingEvent(any());
    }

    @Test
    void handleEntryEvent_shouldThrow_whenVehicleAlreadyParked() {
        when(parkingEventService.isVehicleCurrentlyParked(LICENSE)).thenReturn(true);

        assertThrows(
                VehicleAlreadyParkedException.class,
                () -> eventTypeService.handleEntryEvent(SECTOR, LICENSE, OffsetDateTime.now(GARAGE_ZONE))
        );
    }

    @Test
    void handleEntryEvent_shouldThrow_whenGarageFull() {
        garage.setCurrentOccupancy(10);

        when(parkingEventService.isVehicleCurrentlyParked(LICENSE)).thenReturn(false);
        when(spotService.findFistAvailableSpot(SECTOR)).thenReturn(spot);

        assertThrows(
                GarageFullException.class,
                () -> eventTypeService.handleEntryEvent(SECTOR, LICENSE, OffsetDateTime.now(GARAGE_ZONE))
        );
    }

    @Test
    void handleEntryEvent_shouldThrow_whenGarageClosed() {
        when(parkingEventService.isVehicleCurrentlyParked(LICENSE)).thenReturn(false);
        when(spotService.findFistAvailableSpot(SECTOR)).thenReturn(spot);
        when(garageService.isGarageOpen(any(), any(), any())).thenReturn(false);

        assertThrows(
                GarageClosedException.class,
                () -> eventTypeService.handleEntryEvent(SECTOR, LICENSE, OffsetDateTime.now(GARAGE_ZONE))
        );
    }



    @Test
    void handleParkedEvent_shouldUpdateLatLng() {
        when(parkingEventRepository.findByLicensePlateAndExitTimeIsNull(LICENSE))
                .thenReturn(Optional.of(parkingEvent));
        when(parkingEventService.saveParkingEvent(any())).thenReturn(parkingEvent);

        ParkingEventModel result = eventTypeService.handleParkedEvent(LICENSE, -10.0, 20.0);

        assertEquals(-10.0, result.getLat());
        assertEquals(20.0, result.getLgn());
    }

    @Test
    void handleParkedEvent_shouldThrow_whenNotActiveEventFound() {
        when(parkingEventRepository.findByLicensePlateAndExitTimeIsNull(LICENSE))
                .thenReturn(Optional.empty());

        assertThrows(
                ActiveParkingNotFoundException.class,
                () -> eventTypeService.handleParkedEvent(LICENSE, 0.0, 0.0)
        );
    }



    @Test
    void handleExitEvent_shouldCalculatePriceAndUpdateModels() {

        when(parkingEventService.findParkingEventByLicensePlate(LICENSE)).thenReturn(parkingEvent);
        when(parkingEventService.saveParkingEvent(any())).thenReturn(parkingEvent);
        when(spotService.saveSpot(any())).thenReturn(spot);
        when(garageService.saveGarage(any())).thenReturn(garage);

        OffsetDateTime exitTime = parkingEvent.getEntryTime().plusHours(2);

        ParkingEventModel result = eventTypeService.handleExitEvent(LICENSE, exitTime);

        assertNotNull(result);
        assertEquals(exitTime, result.getExitTime());
        assertTrue(result.getFinalAmount() > 0);
        assertFalse(spot.isOccupied());
    }

    @Test
    void handleExitEvent_shouldThrow_whenInvalidExit() {
        when(parkingEventService.findParkingEventByLicensePlate(LICENSE)).thenReturn(parkingEvent);

        doThrow(new RuntimeException("Invalid time"))
                .when(eventValidationService)
                .validateExitTime(any(), any(), any());

        assertThrows(RuntimeException.class,
                () -> eventTypeService.handleExitEvent(LICENSE, OffsetDateTime.now(GARAGE_ZONE)));
    }
}
