# Sensor Simulators - Implementation Notes

## Overview

This document describes the implementation of sensor simulators for the IoT Sensors REST API project.

## Requirements (from Problem Statement)

Create small programs in Java and C that simulate sensor behavior with the following characteristics:

1. **Timing**: Send data approximately every 30 seconds with a margin of ±10 seconds (randomly between 20-40 seconds)
2. **Data**: Send fictitious temperature, humidity, and atmospheric pressure data
3. **Variable Fields**: Send any combination of 1, 2, or 3 fields randomly
4. **Packet Loss**: Sometimes skip transmission after waiting, simulating packet loss

## Implementation

### Java Simulator (`simulators/java/SensorSimulator.java`)

**Key Features:**
- Standalone Java program with no external dependencies
- Uses `HttpURLConnection` for HTTP POST requests
- Implements random timing with `Thread.sleep()`
- Generates random sensor data within realistic ranges:
  - Temperature: 15-30°C
  - Humidity: 30-80%
  - Pressure: 980-1040 hPa
- 10% probability of packet loss per cycle
- Detailed logging with timestamps

**Compilation:**
```bash
javac SensorSimulator.java
```

**Execution:**
```bash
java SensorSimulator
```

### C Simulator (`simulators/c/sensor_simulator.c`)

**Key Features:**
- C implementation using libcurl for HTTP requests
- Same behavior as Java version
- Efficient JSON building using snprintf with offset tracking
- Proper error handling for network operations
- Uses standard C libraries (stdio, stdlib, string, time, unistd)

**Compilation:**
```bash
make
# or manually:
gcc -Wall -Wextra -O2 -o sensor_simulator sensor_simulator.c -lcurl
```

**Execution:**
```bash
./sensor_simulator
```

**Dependencies:**
- libcurl (install with package manager)

## API Modifications

To support the simulator behavior (sending partial data), the following changes were made to the API:

### 1. Sensor Entity (`src/main/java/com/iot/sensors/model/Sensor.java`)
- Removed `@NotNull` constraints from temperature, humidity, and pressure fields
- Changed columns to nullable in database schema

### 2. SensorRequest DTO (`src/main/java/com/iot/sensors/dto/SensorRequest.java`)
- Removed `@NotNull` validation constraints from sensor data fields
- Only name and location remain required

### 3. Database Schema
Updated to allow NULL values for sensor fields:
```sql
temperature float(53),        -- nullable
humidity float(53),           -- nullable
pressure float(53),           -- nullable
```

## Testing

### Manual Testing

1. Start the API:
```bash
mvn spring-boot:run
```

2. In another terminal, start a simulator:
```bash
# Java
cd simulators/java && javac SensorSimulator.java && java SensorSimulator

# C
cd simulators/c && make && ./sensor_simulator
```

3. Verify data is being stored:
```bash
curl http://localhost:8080/api/sensors
```

### Expected Behavior

The simulator output should show:
- Cycle information
- Wait times varying between 20-40 seconds
- Generated data (1, 2, or 3 fields randomly)
- Occasional packet loss messages
- Success/failure status of transmissions

Example:
```
=== IoT Sensor Simulator (Java) ===
API URL: http://localhost:8080/api/sensors
Sensor: Sensor-Simulator-Java
Location: Test-Lab
Interval: ~30s (±10s)
Starting simulation...

[2025-12-16 07:23:18] Cycle 1 starting...
[2025-12-16 07:23:18] Waiting 40 seconds...
[2025-12-16 07:23:58] Generated data: Humidity=76.72%
[2025-12-16 07:23:58] SUCCESS - Data sent to API

[2025-12-16 07:23:58] Cycle 2 starting...
[2025-12-16 07:23:58] Waiting 33 seconds...
[2025-12-16 07:24:31] Generated data: Temperature=26.89°C, Humidity=72.46%
[2025-12-16 07:24:31] SUCCESS - Data sent to API

[2025-12-16 07:24:31] Cycle 3 starting...
[2025-12-16 07:24:31] Waiting 21 seconds...
[2025-12-16 07:24:52] PACKET LOSS - Skipping transmission
```

## Code Quality

### Code Review
- ✅ All code review feedback addressed
- ✅ Consistent logging with timestamps
- ✅ Efficient string operations (no repeated strlen calls)
- ✅ Proper error handling

### Security
- ✅ CodeQL scan passed (0 vulnerabilities)
- ✅ No hardcoded secrets
- ✅ Input validation in API
- ✅ Proper memory management in C code

### Testing
- ✅ All existing unit tests pass
- ✅ Java simulator tested with live API
- ✅ Partial data correctly stored in database

## Differences Between Java and C Versions

Both implementations provide identical functionality, with minor differences:

| Aspect | Java | C |
|--------|------|---|
| HTTP Library | HttpURLConnection (built-in) | libcurl (external) |
| Compilation | Single command | Requires libcurl-dev |
| Memory Management | Automatic (GC) | Manual |
| Platform | Write once, run anywhere | Platform-specific compilation |
| Performance | Good | Slightly better |

## Configuration

Both simulators can be customized by editing constants at the top of the source files:

- `API_URL`: Target API endpoint
- `SENSOR_NAME`: Identifier for the simulator
- `LOCATION`: Location string
- `BASE_INTERVAL`: Base wait time (30 seconds)
- `TIME_VARIATION`: Random variation (±10 seconds)

## Known Limitations

1. **C Simulator**: Requires libcurl to be installed on the system
2. **No Authentication**: Simulators assume API is open (appropriate for development)
3. **Single Sensor**: Each simulator instance acts as one sensor
4. **No Retry Logic**: Failed transmissions are logged but not retried

## Future Enhancements

Potential improvements for the simulators:

1. Command-line arguments for configuration
2. Multiple concurrent sensors from one process
3. Configurable packet loss rate
4. Retry logic for failed transmissions
5. Support for HTTPS with certificates
6. Logging to file instead of console
7. Statistics reporting (success rate, average timing, etc.)

## Conclusion

Both simulators successfully implement the requirements and provide a realistic way to test the IoT Sensors API with varying data patterns and network conditions.
