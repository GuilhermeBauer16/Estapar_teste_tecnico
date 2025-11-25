package com.github.GuilhermeBauer16.EstaparTesteTecnico.repository;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.GarageModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GarageRepository extends JpaRepository<GarageModel, String> {
}
