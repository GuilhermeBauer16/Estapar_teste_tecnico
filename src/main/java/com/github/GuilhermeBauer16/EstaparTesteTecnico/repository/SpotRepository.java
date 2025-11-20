package com.github.GuilhermeBauer16.EstaparTesteTecnico.repository;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.SpotModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<SpotModel, Long> {
}
