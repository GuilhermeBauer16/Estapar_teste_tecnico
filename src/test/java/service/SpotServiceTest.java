package service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.GarageFullException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.SpotRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.SpotService;
import constants.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.github.GuilhermeBauer16.EstaparTesteTecnico.constants.ValidationConstants.GARAGE_FULL_EXCEPTION_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SpotServiceTest {

    private SpotModel spot;
    private GarageModel mockGarage;


    @Mock
    private SpotRepository spotRepository;

    @InjectMocks
    private SpotService spotService;

    @BeforeEach
    void setUp() {
        mockGarage = new GarageModel(TestConstants.SECTOR, TestConstants.BASE_PRICE, TestConstants.MAX_CAPACITY,
                TestConstants.CURRENT_OCCUPANCY, TestConstants.OPEN_HOUR, TestConstants.CLOSE_HOUR);

        spot = new SpotModel(TestConstants.ID, TestConstants.LAT, TestConstants.LNG, mockGarage,
                TestConstants.IS_OCCUPIED_FALSE, TestConstants.LICENSE_PLATE);

    }


    @Test
    void testSaveSpot_WhenSuccessful_ShouldReturnSpotModelObject() {

        when(spotRepository.save(spot)).thenReturn(spot);

        SpotModel savedSpot = spotService.saveSpot(spot);

        assertNotNull(savedSpot);
        assertFalse(savedSpot.isOccupied());
        assertNotNull(savedSpot.getGarageModel());
        assertEquals(TestConstants.ID, savedSpot.getId());
        assertEquals(TestConstants.LAT, savedSpot.getLat());
        assertEquals(TestConstants.LNG, savedSpot.getLng());
        assertEquals(TestConstants.IS_OCCUPIED_FALSE, savedSpot.isOccupied());
        assertEquals(TestConstants.LICENSE_PLATE, savedSpot.getOccupiedByLicensePlate());
        assertEquals(TestConstants.SECTOR, savedSpot.getGarageModel().getSector());


        verify(spotRepository, times(1)).save(any(SpotModel.class));
    }

    @Test
    void findFirstAvailableSpot_WhenSpotExists_ShouldReturnSpotModel() {

        when(spotRepository.findFirstByGarageModel_SectorAndIsOccupied(TestConstants.SECTOR, false))
                .thenReturn(Optional.of(spot));


        SpotModel foundSpot = spotService.findFistAvailableSpot(TestConstants.SECTOR);


        assertNotNull(foundSpot);
        assertFalse(foundSpot.isOccupied());
        assertNotNull(foundSpot.getGarageModel());
        assertEquals(TestConstants.ID, foundSpot.getId());
        assertEquals(TestConstants.LAT, foundSpot.getLat());
        assertEquals(TestConstants.LNG, foundSpot.getLng());
        assertEquals(TestConstants.IS_OCCUPIED_FALSE, foundSpot.isOccupied());
        assertEquals(TestConstants.LICENSE_PLATE, foundSpot.getOccupiedByLicensePlate());
        assertEquals(TestConstants.SECTOR, foundSpot.getGarageModel().getSector());


        verify(spotRepository, times(1)).findFirstByGarageModel_SectorAndIsOccupied(TestConstants.SECTOR, false);
    }


    @Test
    void findFirstAvailableSpot_WhenGarageIsFull_ShouldThrowGarageFullException() {

        when(spotRepository.findFirstByGarageModel_SectorAndIsOccupied(TestConstants.SECTOR, false))
                .thenReturn(Optional.empty());


        Exception exception = assertThrows(GarageFullException.class, () -> {
            spotService.findFistAvailableSpot(TestConstants.SECTOR);
        });

        assertNotNull(exception);
        assertEquals(GARAGE_FULL_EXCEPTION_MESSAGE, exception.getMessage());


        verify(spotRepository, times(1)).findFirstByGarageModel_SectorAndIsOccupied(TestConstants.SECTOR, false);
    }


}