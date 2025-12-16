package com.iot.sensors.exception;

public class SensorNotFoundException extends RuntimeException {
    
    public SensorNotFoundException(String message) {
        super(message);
    }
    
    public SensorNotFoundException(Long id) {
        super("Sensor not found with id: " + id);
    }
}
