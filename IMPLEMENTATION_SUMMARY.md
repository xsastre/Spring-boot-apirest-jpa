# Implementation Summary - IoT Sensors REST API

## Overview
A complete and functional Spring Boot 4.0.0 REST API for managing IoT sensor readings (temperature, humidity, and pressure) with comprehensive documentation and dual environment support.

## Requirements Met

### ✅ Spring Boot 4.0.0
- Implemented using Spring Boot 4.0.0 with Java 17
- Maven build system configured

### ✅ REST API for IoT Sensors
Implemented complete CRUD operations for sensor data:
- **Temperature**: Numeric value for temperature readings (optional)
- **Humidity**: Numeric value for humidity percentage (optional)
- **Pressure**: Numeric value for atmospheric pressure (optional)
- Supports partial sensor data (any combination of 1, 2, or 3 fields)

### ✅ Sensor Simulators
Implemented sensor simulators to test the API:
- **Java Simulator**: Standalone Java program that simulates IoT sensor behavior
- **C Simulator**: C program using libcurl for HTTP requests
- Both simulators feature:
  - Random timing: ~30 seconds ± 10 seconds between transmissions
  - Fictitious data generation: Temperature (15-30°C), Humidity (30-80%), Pressure (980-1040 hPa)
  - Random field selection: Sends 1, 2, or 3 fields randomly
  - Packet loss simulation: 10% chance of skipping transmission

### ✅ Swagger Documentation
- Integrated SpringDoc OpenAPI 3 (version 2.7.0)
- Available at: `/swagger-ui.html`
- API documentation at: `/api-docs`
- Detailed endpoint descriptions and annotations

### ✅ Two Environments

#### Development Environment
- Database: H2 in-memory database
- Auto-create schema on startup
- Sample data pre-loaded
- H2 console enabled at `/h2-console`
- Profile: `dev` (default)

#### Production Environment
- Database: PostgreSQL
- Manual schema management with SQL scripts
- Connection pooling configured (HikariCP)
- Profile: `prod`

### ✅ SQL Scripts for Database Creation
Created comprehensive SQL scripts:
- `schema.sql`: Generic schema for both environments
- `schema-postgresql.sql`: PostgreSQL-specific creation script with indexes
- `data-dev.sql`: Sample data for development environment

## Database Access Technique

### JPA (Java Persistence API) with Spring Data JPA

This project implements the **JPA standard** for database access, using **Spring Data JPA** to simplify repository implementations.

#### Implementation Details:

1. **JPA Entity Model**
   - `Sensor` entity class annotated with `@Entity`
   - Jakarta Persistence API annotations for ORM mapping
   - Automatic primary key generation using `@GeneratedValue(strategy = GenerationType.IDENTITY)`
   - Lifecycle callbacks with `@PrePersist` and `@PreUpdate`
   - Column mappings with proper constraints

2. **Spring Data JPA Repository Pattern**
   - `SensorRepository` extends `JpaRepository<Sensor, Long>`
   - No implementation code required - Spring generates at runtime
   - Built-in CRUD methods: `save()`, `findById()`, `findAll()`, `deleteById()`
   - Custom derived query methods: `findByLocation()`, `findByName()`
   - Automatic transaction management

3. **Hibernate as JPA Provider**
   - Hibernate 6.x (included with Spring Boot 4.0.0)
   - Automatic DDL generation in development environment
   - HikariCP for connection pooling
   - Support for multiple database dialects (H2, PostgreSQL)

4. **Benefits of this approach**
   - Type-safe database operations
   - Database vendor independence
   - Reduced boilerplate code
   - Automatic entity-to-table mapping
   - Built-in transaction management
   - Easy to test with in-memory databases

## Architecture

### Layered Architecture
1. **Controller Layer**: REST endpoints with Swagger annotations
2. **Service Layer**: Business logic and transaction management
3. **Repository Layer**: JPA data access using Spring Data repositories
4. **Model Layer**: JPA entities with ORM annotations
5. **DTO Layer**: Request/Response data transfer objects
6. **Exception Layer**: Custom exception handling

### Key Components

#### Entity
- **Sensor**: Main entity with fields for name, location, temperature, humidity, pressure, timestamps

#### REST Endpoints
- `POST /api/sensors` - Create new sensor reading
- `GET /api/sensors` - Get all sensor readings
- `GET /api/sensors/{id}` - Get sensor by ID
- `GET /api/sensors/location/{location}` - Get sensors by location
- `GET /api/sensors/name/{name}` - Get sensors by name
- `PUT /api/sensors/{id}` - Update sensor reading
- `DELETE /api/sensors/{id}` - Delete sensor reading

#### Exception Handling
- Custom `SensorNotFoundException` for 404 errors
- `GlobalExceptionHandler` for centralized error handling
- Validation error responses with field-level details

#### Validation
- Input validation using Jakarta Bean Validation
- Required field validation
- Proper HTTP status codes (201, 200, 404, 400)

## Testing

### Unit Tests
- Application context loads successfully
- All tests pass

### Manual Testing
- All CRUD operations verified
- Error handling tested (404, validation errors)
- Swagger UI accessible and functional
- Sample data loaded correctly in development

## Security

### CodeQL Analysis
- ✅ Zero security vulnerabilities found
- All code reviewed and validated

## Project Structure

```
.
├── src/
│   ├── main/
│   │   ├── java/com/iot/sensors/
│   │   │   ├── IotSensorsApplication.java
│   │   │   ├── config/
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller/
│   │   │   │   └── SensorController.java
│   │   │   ├── dto/
│   │   │   │   ├── SensorRequest.java
│   │   │   │   └── SensorResponse.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── SensorNotFoundException.java
│   │   │   ├── model/
│   │   │   │   └── Sensor.java
│   │   │   ├── repository/
│   │   │   │   └── SensorRepository.java
│   │   │   └── service/
│   │   │       └── SensorService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       ├── schema.sql
│   │       ├── schema-postgresql.sql
│   │       └── data-dev.sql
│   └── test/
│       └── java/com/iot/sensors/
│           └── IotSensorsApplicationTests.java
└── simulators/
    ├── README.md
    ├── java/
    │   ├── SensorSimulator.java
    │   └── README.md
    └── c/
        ├── sensor_simulator.c
        ├── Makefile
        └── README.md
```

## How to Run

### Development Mode (H2)
```bash
mvn spring-boot:run
# or
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production Mode (PostgreSQL)
```bash
# First, create database and run schema
psql -U postgres -d iot_sensors_db -f src/main/resources/schema-postgresql.sql

# Then run application
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## API Examples

### Create Sensor Reading (All Fields)
```bash
curl -X POST http://localhost:8080/api/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sensor-001",
    "location": "Laboratory",
    "temperature": 21.5,
    "humidity": 60.0,
    "pressure": 1013.5
  }'
```

### Create Sensor Reading (Partial Data)
```bash
curl -X POST http://localhost:8080/api/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sensor-002",
    "location": "Laboratory",
    "temperature": 22.3
  }'
```

### Get All Sensors
```bash
curl http://localhost:8080/api/sensors
```

### Run Sensor Simulator (Java)
```bash
cd simulators/java
javac SensorSimulator.java
java SensorSimulator
```

### Run Sensor Simulator (C)
```bash
cd simulators/c
make
./sensor_simulator
```

### Error Response Example
```bash
# Non-existent sensor (404)
curl http://localhost:8080/api/sensors/999

# Response:
{
  "status": 404,
  "message": "Sensor not found with id: 999",
  "timestamp": "2025-12-16T06:35:35.155513653"
}
```

## Features Implemented

✅ Full CRUD operations  
✅ Support for partial sensor data (optional fields)  
✅ Input validation with detailed error messages  
✅ Custom exception handling  
✅ Swagger/OpenAPI documentation  
✅ Multiple environment support  
✅ Database initialization scripts  
✅ Sample data for development  
✅ RESTful API design  
✅ Proper HTTP status codes  
✅ Transaction management  
✅ Connection pooling  
✅ Sensor simulators (Java and C)  
✅ Comprehensive README and documentation  

## Quality Assurance

- ✅ Code builds successfully
- ✅ All tests pass
- ✅ Application runs correctly in development mode
- ✅ All API endpoints tested and verified
- ✅ Error handling validated
- ✅ Security scan passed (0 vulnerabilities)
- ✅ Code review feedback implemented

## Documentation

- ✅ Comprehensive README with usage examples
- ✅ Swagger UI for interactive API testing
- ✅ SQL scripts with comments
- ✅ Code comments where appropriate
- ✅ Implementation summary (this document)

## Conclusion

The project is complete and fully functional. All requirements from the problem statement have been met:
- Spring Boot 4.0.0 ✓
- IoT sensors REST API (temperature, humidity, pressure) ✓
- Support for partial sensor data ✓
- Sensor simulators in Java and C ✓
- Swagger documentation ✓
- Two environments (development with H2, production with PostgreSQL) ✓
- SQL scripts for database creation ✓

### Sensor Simulator Implementation Details

The sensor simulators implement the requested behavior:
1. **Timing**: Send data every ~30 seconds with ±10 seconds random variation (20-40 seconds range)
2. **Data Generation**: Generate fictitious values for temperature, humidity, and pressure
3. **Variable Fields**: Randomly send 1, 2, or 3 of the sensor parameters
4. **Packet Loss**: Simulate transmission failures (~10% of cycles)

Both Java and C versions provide identical functionality and can be used interchangeably to test the API.

The application is production-ready and follows Spring Boot best practices.
