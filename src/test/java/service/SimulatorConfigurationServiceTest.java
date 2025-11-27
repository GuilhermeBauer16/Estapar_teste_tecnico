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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
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


    @Test
    void shouldNotCallSimulatorWhenGarageAlreadyExists() {

        when(garageRepository.count()).thenReturn(1L);

        service.fetchAndPersistGarageConfiguration();

        verify(restTemplate, never()).getForObject(anyString(), eq(GarageConfigResponse.class));
    }


    @Test
    void shouldThrowExceptionWhenSimulatorReturnsNull() {

        when(garageRepository.count()).thenReturn(0L);
        when(restTemplate.getForObject(anyString(), eq(GarageConfigResponse.class))).thenReturn(null);

        assertThrows(InvalidSimulatorException.class,
                () -> service.fetchAndPersistGarageConfiguration()
        );
    }


    @Test
    void shouldPersistGaragesSpotsAndParkingEvents() {

        when(garageRepository.count()).thenReturn(0L);


        RawGarageConfigDTO rawGarage = new RawGarageConfigDTO(
                "A",
                10.0,
                50,
                0,
                LocalTime.of(8,0),
                LocalTime.of(18,0)
        );

        RawSpotConfigDTO spotOccupied = new RawSpotConfigDTO();
        spotOccupied.setId(1L);
        spotOccupied.setSector("A");
        spotOccupied.setLat(-10.0);
        spotOccupied.setLng(-20.0);
        spotOccupied.setOccupied(true);

        RawSpotConfigDTO spotFree = new RawSpotConfigDTO();
        spotFree.setId(2L);
        spotFree.setSector("A");
        spotFree.setLat(-10.5);
        spotFree.setLng(-20.5);
        spotFree.setOccupied(false);

        GarageConfigResponse response = new GarageConfigResponse();
        response.setGarage(List.of(rawGarage));
        response.setSpots(List.of(spotOccupied, spotFree));

        when(restTemplate.getForObject(anyString(), eq(GarageConfigResponse.class)))
                .thenReturn(response);


        GarageModel savedGarage = new GarageModel(
                "A",
                rawGarage.getBasePrice(),
                rawGarage.getMaxCapacity(),
                rawGarage.getCurrentOccupancy(),
                rawGarage.getOpenHour(),
                rawGarage.getCloseHour()
        );

        when(garageService.saveGarage(any(GarageModel.class)))
                .thenReturn(savedGarage);


        SpotModel savedOccupiedSpot = new SpotModel(
                1L, -10.0, -20.0, savedGarage, true, "INIT_PLACA_SIM_1"
        );
        SpotModel savedFreeSpot = new SpotModel(
                2L, -10.5, -20.5, savedGarage, false, null
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

        when(garageRepository.count()).thenReturn(0L);
        when(restTemplate.getForObject(anyString(), eq(GarageConfigResponse.class)))
                .thenThrow(new RuntimeException("Simulated error"));

        assertThrows(InvalidSimulatorException.class,
                () -> service.fetchAndPersistGarageConfiguration()
        );
    }
}