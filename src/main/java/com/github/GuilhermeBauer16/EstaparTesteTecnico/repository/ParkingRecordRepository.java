package com.github.GuilhermeBauer16.EstaparTesteTecnico.repository;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingRecordModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRecordRepository extends JpaRepository<ParkingRecordModel,Long> {
}
