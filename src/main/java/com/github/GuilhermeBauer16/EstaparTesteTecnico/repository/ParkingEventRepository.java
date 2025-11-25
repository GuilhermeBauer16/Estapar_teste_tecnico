package com.github.GuilhermeBauer16.EstaparTesteTecnico.repository;

import com.github.GuilhermeBauer16.EstaparTesteTecnico.model.ParkingEventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface ParkingEventRepository extends JpaRepository<ParkingEventModel,Long> {

    @Query("SELECT p FROM ParkingEventModel p \n" +
            "WHERE p.licensePlate = :licensePlate AND p.exitTime IS NULL")
    Optional<ParkingEventModel> findByLicensePlateAndExitTimeIsNull(@Param("licensePlate")String licensePlate);

    @Query("SELECT pem FROM ParkingEventModel pem JOIN pem.garageModel gm " +
            "WHERE gm.sector = :sectorName " +
            "AND pem.exitTime IS NOT NULL " +
            "AND pem.exitTime >= :startOfDay AND pem.exitTime <= :endOfDay")
    List<ParkingEventModel> findCompletedEventsBySectorAndExitDateRange(
            @Param("sectorName") String sectorName,
            @Param("startOfDay") OffsetDateTime startOfDay,
            @Param("endOfDay") OffsetDateTime endOfDay);
}
