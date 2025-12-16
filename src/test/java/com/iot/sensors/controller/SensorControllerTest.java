package com.iot.sensors.controller;

import com.iot.sensors.model.Sensor;
import com.iot.sensors.repository.SensorRepository;
import com.iot.sensors.service.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class SensorControllerTest {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private SensorService sensorService;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        sensorRepository.deleteAll();
    }

    @Test
    void shouldSaveAndRetrieveSensor() {
        // Create test sensor
        Sensor sensor = new Sensor("Test-Sensor", "Test Location", 25.5, 70.0, 1015.0);
        Sensor saved = sensorRepository.save(sensor);

        // Verify
        assertNotNull(saved.getId());
        assertEquals("Test-Sensor", saved.getName());
        assertEquals("Test Location", saved.getLocation());
        assertEquals(25.5, saved.getTemperature());
        assertEquals(70.0, saved.getHumidity());
        assertEquals(1015.0, saved.getPressure());
    }

    @Test
    void shouldFindAllSensors() {
        // Create test sensors
        sensorRepository.save(new Sensor("Sensor-1", "Location-1", 20.0, 60.0, 1013.0));
        sensorRepository.save(new Sensor("Sensor-2", "Location-2", 22.0, 65.0, 1014.0));

        // Verify
        var sensors = sensorRepository.findAll();
        assertEquals(2, sensors.size());
    }

    @Test
    void shouldFindSensorsByLocation() {
        // Create test sensors
        sensorRepository.save(new Sensor("Sensor-A", "Warehouse", 20.0, 60.0, 1013.0));
        sensorRepository.save(new Sensor("Sensor-B", "Warehouse", 21.0, 61.0, 1013.5));
        sensorRepository.save(new Sensor("Sensor-C", "Office", 22.0, 62.0, 1014.0));

        // Verify
        var warehouseSensors = sensorRepository.findByLocation("Warehouse");
        assertEquals(2, warehouseSensors.size());
        
        var officeSensors = sensorRepository.findByLocation("Office");
        assertEquals(1, officeSensors.size());
    }

    @Test
    void shouldUpdateSensor() {
        // Create test sensor
        Sensor sensor = new Sensor("Original", "Location-1", 20.0, 60.0, 1013.0);
        Sensor saved = sensorRepository.save(sensor);

        // Update
        saved.setName("Updated");
        saved.setTemperature(25.0);
        Sensor updated = sensorRepository.save(saved);

        // Verify
        assertEquals("Updated", updated.getName());
        assertEquals(25.0, updated.getTemperature());
    }

    @Test
    void shouldDeleteSensor() {
        // Create test sensor
        Sensor sensor = new Sensor("To-Delete", "Location-1", 20.0, 60.0, 1013.0);
        Sensor saved = sensorRepository.save(sensor);
        Long id = saved.getId();

        // Delete
        sensorRepository.deleteById(id);

        // Verify
        assertFalse(sensorRepository.existsById(id));
    }
}
