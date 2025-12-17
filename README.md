<img src="https://docencia.xaviersastre.cat/Moduls/DWES/img/Spring_Boot_Documentation_Header_1200x200px_with_Xavier_Sastre_Mug_and_Logo_ajustat.png"> 

[![Fet amb Spring Boot](https://img.shields.io/badge/Fet%20amb-Spring%20Boot-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![per en](https://img.shields.io/badge/per%20en-xsastre-red)](https://github.com/xsastre)
[![Desenvolupat al des-2025](https://img.shields.io/badge/Desenvolupat%20al-des--2025-yellow)](https://github.com/xsastre)

# API REST per Sensors IoT - Spring Boot 4.0.0

[English version below](#iot-sensors-rest-api---spring-boot-400)

Una API REST funcional amb Spring Boot per gestionar lectures de sensors IoT (temperatura, humitat i pressió).

## Funcionalitats

- ✅ Spring Boot 4.0.0
- ✅ API REST amb operacions CRUD completes
- ✅ Dades de sensors IoT (temperatura, humitat, pressió)
- ✅ Suport per dades parcials dels sensors (camps opcionals)
- ✅ Simuladors de sensors en Java i C
- ✅ Documentació Swagger/OpenAPI
- ✅ Dos entorns: Desenvolupament (H2) i Producció (PostgreSQL)
- ✅ JPA/Hibernate per persistència
- ✅ Validació d'entrada
- ✅ Scripts SQL per a la creació de la base de dades

## Tecnologies

- **Java 17+**
- **Spring Boot 4.0.0**
- **Spring Data JPA**
- **H2 Database** (desenvolupament)
- **PostgreSQL** (producció)
- **SpringDoc OpenAPI 3** (Swagger UI)
- **Maven**

## Tècnica d'accés a la base de dades

Aquest projecte utilitza **JPA (Java Persistence API)** amb **Spring Data JPA** com a tècnica d'accés a la base de dades.

### Components clau:

1. **Entitats JPA**: La classe `Sensor` està anotada amb `@Entity` i usa anotacions JPA per al mapeig ORM
   - `@Entity` i `@Table` per mapar a taules de la base de dades
   - `@Id` i `@GeneratedValue` per a la gestió de claus primàries
   - `@Column` per al mapeig de camps a columnes
   - `@PrePersist` i `@PreUpdate` per callbacks del cicle de vida

2. **Repositoris Spring Data JPA**: La interfície `SensorRepository` estèn `JpaRepository<Sensor, Long>`
   - Proporciona operacions CRUD automàtiques sense codi repetitiu
   - Mètodes de consulta personalitzats per convenció de nom (per exemple, `findByLocation`, `findByName`)
   - Implementació automàtica en temps d'execució per Spring Data

3. **Hibernate com a proveïdor JPA**: Hibernate s'utilitza com a implementació JPA
   - Generació automàtica d'esquema (DDL) en desenvolupament
   - Gestió de transaccions
   - Pool de connexions via HikariCP

### Avantatges d'aquest enfocament:

- **Orientat a objectes**: Treballar amb objectes Java en lloc d'escriure SQL manual
- **Independència de base de dades**: El mateix codi funciona amb H2 (dev) i PostgreSQL (prod)
- **Menys codi repetitiu**: No cal implementar DAOs
- **Tipatge**: Comprovacions a compilació per consultes
- **CRUD automàtic**: Operacions estàndard disponibles per defecte
- **Consultes personalitzades**: Fàcil d'afegir mètodes de consulta amb noms o JPQL

## Requisits previs

- Java 17 o superior
- Maven 3.6+
- PostgreSQL 12+ (per a l'entorn de producció)

## Inici ràpid

### 1. Clonar el repositori

```bash
git clone https://github.com/xsastre/Spring-boot-apirest-jpa.git
cd Spring-boot-apirest-jpa
```

### 2. Compilar el projecte

```bash
mvn clean install
```

### 3. Executar en entorn de desenvolupament (H2)

```bash
mvn spring-boot:run
```

O amb perfil explícit:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

L'aplicació s'iniciarà a `http://localhost:8080`

### 4. Executar en entorn de producció (PostgreSQL)

Primer, assegurar que PostgreSQL està en marxa i crear la base de dades:

```bash
psql -U postgres
CREATE DATABASE iot_sensors_db;
\q
```

Executar l'script d'esquema:

```bash
psql -U postgres -d iot_sensors_db -f src/main/resources/schema-postgresql.sql
```

Actualitzar credencials a `src/main/resources/application-prod.properties` si cal.

Executar l'aplicació:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Documentació de l'API

### Swagger UI

Un cop l'aplicació està en marxa, accedir a Swagger UI a:

```
http://localhost:8080/swagger-ui.html
```

### Endpoints de l'API

| Mètode | Endpoint                      | Descripció                     |
|--------|-------------------------------|---------------------------------|
| POST   | `/api/sensors`                | Crear una nova lectura de sensor|
| GET    | `/api/sensors`                | Obtenir totes les lectures      |
| GET    | `/api/sensors/{id}`           | Obtenir sensor per ID           |
| GET    | `/api/sensors/location/{loc}` | Obtenir sensors per ubicació    |
| GET    | `/api/sensors/name/{name}`    | Obtenir sensors per nom         |
| PUT    | `/api/sensors/{id}`           | Actualitzar una lectura         |
| DELETE | `/api/sensors/{id}`           | Eliminar una lectura            |

### Exemple de petició

**Crear una lectura de sensor amb tots els camps:**

```bash
curl -X POST http://localhost:8080/api/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sensor-006",
    "location": "Laboratori",
    "temperature": 21.5,
    "humidity": 60.0,
    "pressure": 1013.5
  }'
```

**Crear una lectura de sensor amb dades parcials (camps opcionals):**

```bash
curl -X POST http://localhost:8080/api/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sensor-007",
    "location": "Laboratori",
    "temperature": 22.3
  }'
```

**Obtenir tots els sensors:**

```bash
curl http://localhost:8080/api/sensors
```

## Esquema de la base de dades

### Taula sensors

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

## Configuració d'entorns

### Desenvolupament (H2)

- Base de dades: H2 en memòria
- Consola: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:iot_sensors_db`
- Usuari: `sa`
- Contrasenya: (buida)
- Esquema auto-creat: Sí
- Dades mostral: Sí

### Producció (PostgreSQL)

- Base de dades: PostgreSQL
- JDBC URL: `jdbc:postgresql://localhost:5432/iot_sensors_db`
- Usuari: `postgres` (configurable)
- Contrasenya: `postgres` (configurable)
- Gestió d'esquema: Manual (amb scripts SQL)

## Estructura del projecte

```
src/
├── main/
│   ├── java/com/iot/sensors/
│   │   ├── IotSensorsApplication.java    # Aplicació principal
│   │   ├── config/
│   │   │   └── OpenApiConfig.java        # Configuració Swagger
│   │   ├── controller/
│   │   │   └── SensorController.java     # Endpoints REST
│   │   ├── dto/
│   │   │   ├── SensorRequest.java        # DTO de petició
│   │   │   └── SensorResponse.java       # DTO de resposta
│   │   ├── model/
│   │   │   └── Sensor.java               # Entitat JPA
│   │   ├── repository/
│   │   │   └── SensorRepository.java     # Accés a dades
│   │   └── service/
│   │       └── SensorService.java        # Lògica de negoci
│   └── resources/
│       ├── application.properties         # Config principal
│       ├── application-dev.properties     # Config dev
│       ├── application-prod.properties    # Config prod
│       ├── schema.sql                     # Esquema genèric
│       ├── schema-postgresql.sql          # Esquema PostgreSQL
│       └── data-dev.sql                   # Dades mostral dev
└── test/
    └── java/com/iot/sensors/              # Tests
```

## Simuladors de sensors

Aquest projecte inclou simuladors de sensors en Java i C per provar l'API simulant el comportament real d'un sensor:

- **Ubicació**: `simulators/`
- **Característiques**:
    - Envia dades cada ~30 segons (amb variació aleatòria de ±10 segons)
    - Genera lectures fictícies de temperatura, humitat i pressió
    - Envia aleatòriament 1, 2 o 3 paràmetres
    - Simula pèrdua de paquets (10% de probabilitat d'ometre una transmissió)

Vegeu `simulators/README.md` per instruccions detallades de compilació i execució.

## Proves

Executar proves amb:

```bash
mvn test
```

## Compilar per producció

Crear el JAR:

```bash
mvn clean package
```

Executar el JAR:

```bash
java -jar target/spring-boot-iot-api-1.0.0.jar --spring.profiles.active=prod
```

## Llicència

Apache 2.0

## Autor

Xavier Sastre

____

# IoT Sensors REST API - Spring Boot 4.0.0

A functional Spring Boot REST API for managing IoT sensor readings (temperature, humidity, and pressure).

## Features

- ✅ Spring Boot 4.0.0
- ✅ REST API with full CRUD operations
- ✅ IoT Sensors data (temperature, humidity, pressure)
- ✅ Support for partial sensor data (optional fields)
- ✅ Sensor simulators in Java and C
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

## Database Access Technique / Tècnica d'Accés a Base de Dades

This project uses **JPA (Java Persistence API)** with **Spring Data JPA** as the database access technique.

### Key Components:

1. **JPA Entities**: The `Sensor` class is annotated with `@Entity` and uses JPA annotations for object-relational mapping
   - `@Entity` and `@Table` for mapping to database tables
   - `@Id` and `@GeneratedValue` for primary key management
   - `@Column` for field-to-column mapping
   - `@PrePersist` and `@PreUpdate` for lifecycle callbacks

2. **Spring Data JPA Repositories**: The `SensorRepository` interface extends `JpaRepository<Sensor, Long>`
   - Provides automatic CRUD operations without boilerplate code
   - Custom query methods using method naming conventions (e.g., `findByLocation`, `findByName`)
   - Automatic implementation at runtime by Spring Data

3. **Hibernate as JPA Provider**: Hibernate is used as the underlying JPA implementation
   - Automatic schema generation (DDL) in development
   - Transaction management
   - Connection pooling via HikariCP

### Advantages of this approach:

- **Object-Oriented**: Work with Java objects instead of SQL statements
- **Database Independence**: Same code works with H2 (dev) and PostgreSQL (prod)
- **Reduced Boilerplate**: No need to write DAO implementations
- **Type Safety**: Compile-time checking of queries
- **Automatic CRUD**: Standard operations provided out-of-the-box
- **Custom Queries**: Easy to add custom query methods using method names or JPQL

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

**Create a sensor reading with all fields:**

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

**Create a sensor reading with partial data (optional fields):**

```bash
curl -X POST http://localhost:8080/api/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sensor-007",
    "location": "Laboratory",
    "temperature": 22.3
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

## Sensor Simulators

This project includes sensor simulators in Java and C that can be used to test the API by simulating real IoT sensor behavior:

- **Location**: `simulators/` directory
- **Features**:
  - Send data every ~30 seconds (with ±10 seconds random variation)
  - Generate fictitious temperature, humidity, and pressure readings
  - Randomly send 1, 2, or 3 parameters
  - Simulate packet loss (10% chance of skipping transmission)

See [simulators/README.md](simulators/README.md) for detailed instructions on how to compile and run the simulators.

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

Xavier Sastre
