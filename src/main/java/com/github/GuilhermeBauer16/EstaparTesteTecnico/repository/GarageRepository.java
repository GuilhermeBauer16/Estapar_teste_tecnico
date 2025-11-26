package com.github.GuilhermeBauer16.EstaparTesteTecnico.repository;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GarageRepository extends JpaRepository<GarageModel, String> {

    Optional<GarageModel> findBySector(String sector);
}
