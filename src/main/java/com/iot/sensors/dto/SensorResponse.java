package com.iot.sensors.dto;

import com.iot.sensors.model.Sensor;
import java.time.LocalDateTime;

public class SensorResponse {

    private Long id;
    private String name;
    private String location;
    private Double temperature;
    private Double humidity;
    private Double pressure;
    private LocalDateTime measurementTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SensorResponse() {
    }

    public SensorResponse(Sensor sensor) {
        this.id = sensor.getId();
        this.name = sensor.getName();
        this.location = sensor.getLocation();
        this.temperature = sensor.getTemperature();
        this.humidity = sensor.getHumidity();
        this.pressure = sensor.getPressure();
        this.measurementTime = sensor.getMeasurementTime();
        this.createdAt = sensor.getCreatedAt();
        this.updatedAt = sensor.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public LocalDateTime getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(LocalDateTime measurementTime) {
        this.measurementTime = measurementTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
