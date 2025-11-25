package com.github.GuilhermeBauer16.EstaparTesteTecnico.repository;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpotRepository extends JpaRepository<SpotModel, Long> {

    Optional<SpotModel> findFirstByIsOccupied(boolean isOccupied);

    Optional<SpotModel> findByOccupiedByLicensePlate(String licensePlate);
}
