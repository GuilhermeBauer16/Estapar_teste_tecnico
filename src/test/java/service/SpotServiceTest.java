package service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.GarageFullException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.SpotRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.SpotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
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

    @Mock
    private SpotRepository spotRepository;

    @InjectMocks
    private SpotService spotService;

    @BeforeEach
    void setUp() {
        mockGarage = new GarageModel(SECTOR, BASE_PRICE, MAX_CAPACITY, CURRENT_OCCUPANCY, OPEN_HOUR, CLOSE_HOUR);
        spot = new SpotModel(ID, LAT, LNG, mockGarage, IS_OCCUPIED, LICENSE_PLATE);

    }


    @Test
    void testSaveSpot_WhenSuccessful_ShouldReturnSpotModelObject() {

        when(spotRepository.save(spot)).thenReturn(spot);

        SpotModel savedSpot = spotService.saveSpot(spot);

        assertNotNull(savedSpot);
        assertFalse(savedSpot.isOccupied());
        assertNotNull(savedSpot.getGarageModel());
        assertEquals(ID, savedSpot.getId());
        assertEquals(LAT, savedSpot.getLat());
        assertEquals(LNG, savedSpot.getLng());
        assertEquals(IS_OCCUPIED, savedSpot.isOccupied());
        assertEquals(LICENSE_PLATE, savedSpot.getOccupiedByLicensePlate());
        assertEquals(SECTOR, savedSpot.getGarageModel().getSector());


        verify(spotRepository, times(1)).save(any(SpotModel.class));
    }

    @Test
    void findFirstAvailableSpot_WhenSpotExists_ShouldReturnSpotModel() {

        when(spotRepository.findFirstByGarageModel_SectorAndIsOccupied(SECTOR, false))
                .thenReturn(Optional.of(spot));


        SpotModel foundSpot = spotService.findFistAvailableSpot(SECTOR);


        assertNotNull(foundSpot);
        assertFalse(foundSpot.isOccupied());
        assertNotNull(foundSpot.getGarageModel());
        assertEquals(ID, foundSpot.getId());
        assertEquals(LAT, foundSpot.getLat());
        assertEquals(LNG, foundSpot.getLng());
        assertEquals(IS_OCCUPIED, foundSpot.isOccupied());
        assertEquals(LICENSE_PLATE, foundSpot.getOccupiedByLicensePlate());
        assertEquals(SECTOR, foundSpot.getGarageModel().getSector());


        verify(spotRepository, times(1)).findFirstByGarageModel_SectorAndIsOccupied(SECTOR, false);
    }


    @Test
    void findFirstAvailableSpot_WhenGarageIsFull_ShouldThrowGarageFullException() {

        when(spotRepository.findFirstByGarageModel_SectorAndIsOccupied(SECTOR, false))
                .thenReturn(Optional.empty());


        Exception exception = assertThrows(GarageFullException.class, () -> {
            spotService.findFistAvailableSpot(SECTOR);
        });

        assertNotNull(exception);
        assertEquals(GARAGE_FULL_EXCEPTION_MESSAGE, exception.getMessage());


        verify(spotRepository, times(1)).findFirstByGarageModel_SectorAndIsOccupied(SECTOR, false);
    }


}