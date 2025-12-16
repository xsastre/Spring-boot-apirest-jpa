package com.iot.sensors.repository;

import com.iot.sensors.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    
    List<Sensor> findByLocation(String location);
    
    List<Sensor> findByName(String name);
}
