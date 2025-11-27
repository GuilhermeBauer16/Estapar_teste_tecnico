package service;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.GarageConfigResponse;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.RawGarageConfigDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.dto.RawSpotConfigDTO;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.exception.InvalidSimulatorException;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.GarageRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.repository.ParkingEventRepository;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.GarageService;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.SimulatorConfigurationService;
import com.github.GuilhermeBauer16.EstaparTesteTecnico.service.SpotService;
import constants.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimulatorConfigurationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private GarageService garageService;

    @Mock
    private SpotService spotService;

    @Mock
    private ParkingEventRepository parkingEventRepository;

    @InjectMocks
    private SimulatorConfigurationService service;

    private static final Long SECOND_ID = 2L;
    private static final Long ZERO_ID = 0L;


    @Test
    void shouldNotCallSimulatorWhenGarageAlreadyExists() {

        when(garageRepository.count()).thenReturn(TestConstants.ID);

        service.fetchAndPersistGarageConfiguration();

        verify(restTemplate, never()).getForObject(anyString(), eq(GarageConfigResponse.class));
    }


    @Test
    void shouldThrowExceptionWhenSimulatorReturnsNull() {

        when(garageRepository.count()).thenReturn(ZERO_ID);
        when(restTemplate.getForObject(anyString(), eq(GarageConfigResponse.class))).thenReturn(null);

        assertThrows(InvalidSimulatorException.class,
                () -> service.fetchAndPersistGarageConfiguration()
        );
    }


    @Test
    void shouldPersistGaragesSpotsAndParkingEvents() {

        when(garageRepository.count()).thenReturn(ZERO_ID);


        RawGarageConfigDTO rawGarage = new RawGarageConfigDTO(
                TestConstants.SECTOR,
                TestConstants.BASE_PRICE,
                TestConstants.MAX_CAPACITY,
                TestConstants.CURRENT_OCCUPANCY,
                TestConstants.OPEN_HOUR,
                TestConstants.CLOSE_HOUR
        );

        RawSpotConfigDTO spotOccupied = new RawSpotConfigDTO();
        spotOccupied.setId(TestConstants.ID);
        spotOccupied.setSector(TestConstants.SECTOR);
        spotOccupied.setLat(TestConstants.LAT);
        spotOccupied.setLng(TestConstants.LNG);
        spotOccupied.setOccupied(TestConstants.IS_OCCUPIED_TRUE);

        RawSpotConfigDTO spotFree = new RawSpotConfigDTO();
        spotFree.setId(SECOND_ID);
        spotFree.setSector(TestConstants.SECTOR);
        spotFree.setLat(TestConstants.LAT);
        spotFree.setLng(TestConstants.LNG);
        spotFree.setOccupied(TestConstants.IS_OCCUPIED_FALSE);

        GarageConfigResponse response = new GarageConfigResponse();
        response.setGarage(List.of(rawGarage));
        response.setSpots(List.of(spotOccupied, spotFree));

        when(restTemplate.getForObject(anyString(), eq(GarageConfigResponse.class)))
                .thenReturn(response);


        GarageModel savedGarage = new GarageModel(
                TestConstants.SECTOR,
                rawGarage.getBasePrice(),
                rawGarage.getMaxCapacity(),
                rawGarage.getCurrentOccupancy(),
                rawGarage.getOpenHour(),
                rawGarage.getCloseHour()
        );

        when(garageService.saveGarage(any(GarageModel.class)))
                .thenReturn(savedGarage);


        SpotModel savedOccupiedSpot = new SpotModel(
                TestConstants.ID, TestConstants.LAT, TestConstants.LNG, savedGarage, TestConstants.IS_OCCUPIED_TRUE, TestConstants.LICENSE_PLATE
        );
        SpotModel savedFreeSpot = new SpotModel(
                SECOND_ID, TestConstants.LAT, TestConstants.LNG, savedGarage, TestConstants.IS_OCCUPIED_FALSE, null
        );

        when(spotService.saveSpot(any())).thenReturn(savedOccupiedSpot, savedFreeSpot);

        service.fetchAndPersistGarageConfiguration();


        verify(garageService, times(1))
                .saveGarage(any(GarageModel.class));


        verify(spotService, times(2))
                .saveSpot(any());


        verify(parkingEventRepository, times(1))
                .save(any());


        verify(garageRepository, times(1))
                .save(savedGarage);
    }


    @Test
    void shouldThrowInvalidSimulatorExceptionWhenRestTemplateFails() {

        when(garageRepository.count()).thenReturn(ZERO_ID);
        when(restTemplate.getForObject(anyString(), eq(GarageConfigResponse.class)))
                .thenThrow(new RuntimeException("Simulated error"));

        assertThrows(InvalidSimulatorException.class,
                () -> service.fetchAndPersistGarageConfiguration()
        );
    }
}