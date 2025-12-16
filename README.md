# IoT Sensors REST API - Spring Boot 4.0.0

A functional Spring Boot REST API for managing IoT sensor readings (temperature, humidity, and pressure).

## Features

- ✅ Spring Boot 4.0.0
- ✅ REST API with full CRUD operations
- ✅ IoT Sensors data (temperature, humidity, pressure)
- ✅ Swagger/OpenAPI documentation
- ✅ Two environments: Development (H2) and Production (PostgreSQL)
- ✅ JPA/Hibernate for persistence
- ✅ Input validation
- ✅ SQL scripts for database creation

## Technologies

- **Java 17+**
- **Spring Boot 4.0.0**
- **Spring Data JPA**
- **H2 Database** (development)
- **PostgreSQL** (production)
- **SpringDoc OpenAPI 3** (Swagger UI)
- **Maven**

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (for production environment)

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/xsastre/Spring-boot-apirest-jpa.git
cd Spring-boot-apirest-jpa
```

### 2. Build the project

```bash
mvn clean install
```

### 3. Run in Development Environment (H2)

```bash
mvn spring-boot:run
```

Or with explicit profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The application will start on `http://localhost:8080`

### 4. Run in Production Environment (PostgreSQL)

First, ensure PostgreSQL is running and create the database:

```bash
psql -U postgres
CREATE DATABASE iot_sensors_db;
\q
```

Run the schema creation script:

```bash
psql -U postgres -d iot_sensors_db -f src/main/resources/schema-postgresql.sql
```

Update database credentials in `src/main/resources/application-prod.properties` if needed.

Run the application:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## API Documentation

### Swagger UI

Once the application is running, access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

### API Endpoints

| Method | Endpoint                      | Description                    |
|--------|-------------------------------|--------------------------------|
| POST   | `/api/sensors`                | Create a new sensor reading    |
| GET    | `/api/sensors`                | Get all sensor readings        |
| GET    | `/api/sensors/{id}`           | Get sensor by ID               |
| GET    | `/api/sensors/location/{loc}` | Get sensors by location        |
| GET    | `/api/sensors/name/{name}`    | Get sensors by name            |
| PUT    | `/api/sensors/{id}`           | Update sensor reading          |
| DELETE | `/api/sensors/{id}`           | Delete sensor reading          |

### Example Request

**Create a sensor reading:**

```bash
curl -X POST http://localhost:8080/api/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sensor-006",
    "location": "Laboratory",
    "temperature": 21.5,
    "humidity": 60.0,
    "pressure": 1013.5
  }'
```

**Get all sensors:**

```bash
curl http://localhost:8080/api/sensors
```

## Database Schema

### Sensors Table

```sql
CREATE TABLE sensors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    temperature DOUBLE PRECISION NOT NULL,
    humidity DOUBLE PRECISION NOT NULL,
    pressure DOUBLE PRECISION NOT NULL,
    measurement_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
```

## Environment Configuration

### Development (H2)

- Database: In-memory H2
- Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:iot_sensors_db`
- Username: `sa`
- Password: (empty)
- Auto-create schema: Yes
- Sample data loaded: Yes

### Production (PostgreSQL)

- Database: PostgreSQL
- JDBC URL: `jdbc:postgresql://localhost:5432/iot_sensors_db`
- Username: `postgres` (configurable)
- Password: `postgres` (configurable)
- Schema management: Manual (using SQL scripts)

## Project Structure

```
src/
├── main/
│   ├── java/com/iot/sensors/
│   │   ├── IotSensorsApplication.java    # Main application
│   │   ├── config/
│   │   │   └── OpenApiConfig.java        # Swagger configuration
│   │   ├── controller/
│   │   │   └── SensorController.java     # REST endpoints
│   │   ├── dto/
│   │   │   ├── SensorRequest.java        # Request DTO
│   │   │   └── SensorResponse.java       # Response DTO
│   │   ├── model/
│   │   │   └── Sensor.java               # JPA Entity
│   │   ├── repository/
│   │   │   └── SensorRepository.java     # Data access
│   │   └── service/
│   │       └── SensorService.java        # Business logic
│   └── resources/
│       ├── application.properties         # Main config
│       ├── application-dev.properties     # Dev config
│       ├── application-prod.properties    # Prod config
│       ├── schema.sql                     # Generic schema
│       ├── schema-postgresql.sql          # PostgreSQL schema
│       └── data-dev.sql                   # Dev sample data
└── test/
    └── java/com/iot/sensors/              # Test classes
```

## Testing

Run tests with:

```bash
mvn test
```

## Building for Production

Build the JAR file:

```bash
mvn clean package
```

Run the JAR:

```bash
java -jar target/spring-boot-iot-api-1.0.0.jar --spring.profiles.active=prod
```

## License

Apache 2.0

## Author

IoT Sensors Team
