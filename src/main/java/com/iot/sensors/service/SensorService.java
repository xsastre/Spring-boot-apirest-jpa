package com.iot.sensors.service;

import com.iot.sensors.dto.SensorRequest;
import com.iot.sensors.dto.SensorResponse;
import com.iot.sensors.model.Sensor;
import com.iot.sensors.repository.SensorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SensorService {

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public SensorResponse createSensor(SensorRequest request) {
        Sensor sensor = new Sensor(
                request.getName(),
                request.getLocation(),
                request.getTemperature(),
                request.getHumidity(),
                request.getPressure()
        );
        Sensor savedSensor = sensorRepository.save(sensor);
        return new SensorResponse(savedSensor);
    }

    public List<SensorResponse> getAllSensors() {
        return sensorRepository.findAll().stream()
                .map(SensorResponse::new)
                .collect(Collectors.toList());
    }

    public SensorResponse getSensorById(Long id) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + id));
        return new SensorResponse(sensor);
    }

    public List<SensorResponse> getSensorsByLocation(String location) {
        return sensorRepository.findByLocation(location).stream()
                .map(SensorResponse::new)
                .collect(Collectors.toList());
    }

    public List<SensorResponse> getSensorsByName(String name) {
        return sensorRepository.findByName(name).stream()
                .map(SensorResponse::new)
                .collect(Collectors.toList());
    }

    public SensorResponse updateSensor(Long id, SensorRequest request) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + id));
        
        sensor.setName(request.getName());
        sensor.setLocation(request.getLocation());
        sensor.setTemperature(request.getTemperature());
        sensor.setHumidity(request.getHumidity());
        sensor.setPressure(request.getPressure());
        
        Sensor updatedSensor = sensorRepository.save(sensor);
        return new SensorResponse(updatedSensor);
    }

    public void deleteSensor(Long id) {
        if (!sensorRepository.existsById(id)) {
            throw new RuntimeException("Sensor not found with id: " + id);
        }
        sensorRepository.deleteById(id);
    }
}
