package service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.GarageRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.GarageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GarageServiceTest {

    private GarageModel garage;
    private final String SECTOR = "A1";
    private final Double BASE_PRICE = 10.0;
    private final int MAX_CAPACITY = 100;
    private final Integer CURRENT_OCCUPANCY = 50;
    private final LocalTime OPEN_HOUR = LocalTime.of(8, 0);
    private final LocalTime CLOSE_HOUR = LocalTime.of(20, 0);

    @Mock
    private GarageRepository garageRepository;

    @InjectMocks
    private GarageService garageService;

    @BeforeEach
    void setup() {
        garage = new GarageModel(SECTOR, BASE_PRICE, MAX_CAPACITY, CURRENT_OCCUPANCY, OPEN_HOUR, CLOSE_HOUR);
    }


    @Test
    void testSaveGarage_WhenSuccessful_ShouldReturnGarageModelObject() {

        when(garageRepository.save(garage)).thenReturn(garage);

        GarageModel result = garageService.saveGarage(garage);

        assertNotNull(result);
        assertNotNull(garage.getSector());
        assertEquals(SECTOR, garage.getSector());
        assertEquals(BASE_PRICE, garage.getBasePrice());
        assertEquals(CURRENT_OCCUPANCY, garage.getCurrentOccupancy());
        assertEquals(MAX_CAPACITY, garage.getMaxCapacity());
        assertEquals(OPEN_HOUR, garage.getOpenHour());
        assertEquals(CLOSE_HOUR, garage.getCloseHour());


        verify(garageRepository, times(1)).save(any(GarageModel.class));
    }


    @Test
    void testIsGarageOpen_NormalHours_InsideRange() {

        LocalTime open = LocalTime.of(8, 0);
        LocalTime close = LocalTime.of(18, 0);
        LocalTime entry = LocalTime.of(10, 0);

        assertTrue(garageService.isGarageOpen(entry, open, close));
    }

    @Test
    void testIsGarageOpen_NormalHours_OutsideRange() {

        LocalTime open = LocalTime.of(8, 0);
        LocalTime close = LocalTime.of(18, 0);
        LocalTime entry = LocalTime.of(20, 0);

        assertFalse(garageService.isGarageOpen(entry, open, close));
    }

    @Test
    void testIsGarageOpen_OverMidnight_InsideBeforeMidnight() {

        LocalTime open = LocalTime.of(20, 0);
        LocalTime close = LocalTime.of(5, 0);
        LocalTime entry = LocalTime.of(22, 0);

        assertTrue(garageService.isGarageOpen(entry, open, close));
    }

    @Test
    void testIsGarageOpen_OverMidnight_InsideAfterMidnight() {

        LocalTime open = LocalTime.of(20, 0);
        LocalTime close = LocalTime.of(5, 0);
        LocalTime entry = LocalTime.of(3, 0);

        assertTrue(garageService.isGarageOpen(entry, open, close));
    }

    @Test
    void testIsGarageOpen_OverMidnight_OutsideRange() {

        LocalTime open = LocalTime.of(20, 0);
        LocalTime close = LocalTime.of(5, 0);
        LocalTime entry = LocalTime.of(12, 0);

        assertFalse(garageService.isGarageOpen(entry, open, close));
    }

    @Test
    void testIsGarageOpen_NormalHours_OnBoundaries() {
        LocalTime open = LocalTime.of(8, 0);
        LocalTime close = LocalTime.of(18, 0);


        LocalTime entryOnOpen = LocalTime.of(8, 0);
        assertTrue(garageService.isGarageOpen(entryOnOpen, open, close));


        LocalTime entryOnClose = LocalTime.of(18, 0);
        assertTrue(garageService.isGarageOpen(entryOnClose, open, close)
        );
    }

    @Test
    void testIsGarageOpen_TwentyFourHours() {
        LocalTime open = LocalTime.of(0, 0);
        LocalTime close = LocalTime.of(0, 0);
        LocalTime entry = LocalTime.of(15, 30);


        assertFalse(garageService.isGarageOpen(entry, open, close));
    }

}

