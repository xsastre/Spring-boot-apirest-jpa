package com.iot.sensors.controller;

import com.iot.sensors.dto.SensorRequest;
import com.iot.sensors.dto.SensorResponse;
import com.iot.sensors.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@Tag(name = "Sensor", description = "IoT Sensor Management API")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Operation(summary = "Create a new sensor reading", description = "Creates a new sensor reading with temperature, humidity, and pressure data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sensor reading created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<SensorResponse> createSensor(@Valid @RequestBody SensorRequest request) {
        SensorResponse response = sensorService.createSensor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all sensor readings", description = "Retrieves all sensor readings from the database")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping
    public ResponseEntity<List<SensorResponse>> getAllSensors() {
        List<SensorResponse> sensors = sensorService.getAllSensors();
        return ResponseEntity.ok(sensors);
    }

    @Operation(summary = "Get sensor by ID", description = "Retrieves a specific sensor reading by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensor found"),
            @ApiResponse(responseCode = "404", description = "Sensor not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SensorResponse> getSensorById(
            @Parameter(description = "Sensor ID") @PathVariable Long id) {
        SensorResponse response = sensorService.getSensorById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get sensors by location", description = "Retrieves all sensor readings from a specific location")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/location/{location}")
    public ResponseEntity<List<SensorResponse>> getSensorsByLocation(
            @Parameter(description = "Location name") @PathVariable String location) {
        List<SensorResponse> sensors = sensorService.getSensorsByLocation(location);
        return ResponseEntity.ok(sensors);
    }

    @Operation(summary = "Get sensors by name", description = "Retrieves all sensor readings with a specific sensor name")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/name/{name}")
    public ResponseEntity<List<SensorResponse>> getSensorsByName(
            @Parameter(description = "Sensor name") @PathVariable String name) {
        List<SensorResponse> sensors = sensorService.getSensorsByName(name);
        return ResponseEntity.ok(sensors);
    }

    @Operation(summary = "Update sensor reading", description = "Updates an existing sensor reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensor updated successfully"),
            @ApiResponse(responseCode = "404", description = "Sensor not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SensorResponse> updateSensor(
            @Parameter(description = "Sensor ID") @PathVariable Long id,
            @Valid @RequestBody SensorRequest request) {
        SensorResponse response = sensorService.updateSensor(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete sensor reading", description = "Deletes a sensor reading by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sensor deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Sensor not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(
            @Parameter(description = "Sensor ID") @PathVariable Long id) {
        sensorService.deleteSensor(id);
        return ResponseEntity.noContent().build();
    }
}
