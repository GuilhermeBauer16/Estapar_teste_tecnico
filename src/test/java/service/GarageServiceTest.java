package service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.GarageRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.GarageService;
import constants.TestConstants;
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

    @Mock
    private GarageRepository garageRepository;

    @InjectMocks
    private GarageService garageService;

    @BeforeEach
    void setup() {
        garage = new GarageModel(TestConstants.SECTOR, TestConstants.BASE_PRICE, TestConstants.MAX_CAPACITY,
                TestConstants.CURRENT_OCCUPANCY, TestConstants.OPEN_HOUR, TestConstants.CLOSE_HOUR);
    }


    @Test
    void testSaveGarage_WhenSuccessful_ShouldReturnGarageModelObject() {

        when(garageRepository.save(garage)).thenReturn(garage);

        GarageModel result = garageService.saveGarage(garage);

        assertNotNull(result);
        assertNotNull(garage.getSector());
        assertEquals(TestConstants.SECTOR, garage.getSector());
        assertEquals(TestConstants.BASE_PRICE, garage.getBasePrice());
        assertEquals(TestConstants.CURRENT_OCCUPANCY, garage.getCurrentOccupancy());
        assertEquals(TestConstants.MAX_CAPACITY, garage.getMaxCapacity());
        assertEquals(TestConstants.OPEN_HOUR, garage.getOpenHour());
        assertEquals(TestConstants.CLOSE_HOUR, garage.getCloseHour());


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

