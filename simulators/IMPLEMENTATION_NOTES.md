# Simuladors de Sensors - Notes d'Implementació

[English version below](#sensor-simulators---implementation-notes)

## Visió general

Aquest document descriu la implementació dels simuladors de sensors per al projecte IoT Sensors REST API.

## Requisits (del enunciat)

Crear petits programes en Java i C que simulin el comportament d'un sensor amb les següents característiques:

1. **Temporització**: Enviar dades aproximadament cada 30 segons amb un marge de ±10 segons (aleatòriament entre 20-40 segons)
2. **Dades**: Enviar dades fictícies de temperatura, humitat i pressió atmosfèrica
3. **Camps variables**: Enviar qualsevol combinació d'1, 2 o 3 camps de manera aleatòria
4. **Pèrdua de paquets**: De vegades s'ha d'ometre la transmissió després d'esperar, simulant pèrdua de paquets

## Implementació

### Simulador Java (`simulators/java/SensorSimulator.java`)

**Característiques clau:**
- Programa Java autònom sense dependències externes
- Utilitza `HttpURLConnection` per a les peticions HTTP POST
- Implementa temporització aleatòria amb `Thread.sleep()`
- Genera dades de sensor aleatòries dins rangs realistes:
  - Temperatura: 15-30°C
  - Humitat: 30-80%
  - Pressió: 980-1040 hPa
- Probabilitat del 10% de pèrdua de paquets per cicle
- Registre detallat amb marques de temps

**Compilació:**
```bash
javac SensorSimulator.java
```

**Execució:**
```bash
java SensorSimulator
```

### Simulador C (`simulators/c/sensor_simulator.c`)

**Característiques clau:**
- Implementació en C que utilitza libcurl per a peticions HTTP
- Mateix comportament que la versió Java
- Construcció eficient del JSON usant `snprintf` amb seguiment d'offset
- Maneig d'errors adequat per a operacions de xarxa
- Utilitza biblioteques estàndard de C (stdio, stdlib, string, time, unistd)

**Compilació:**
```bash
make
# o manualment:
gcc -Wall -Wextra -O2 -o sensor_simulator sensor_simulator.c -lcurl
```

**Execució:**
```bash
./sensor_simulator
```

**Dependències:**
- libcurl (instal·lar amb el gestor de paquets)

## Modificacions a l'API

Per donar suport al comportament del simulador (enviament de dades parcials), s'han realitzat els següents canvis a l'API:

### 1. Entitat Sensor (`src/main/java/com/iot/sensors/model/Sensor.java`)
- S'han eliminat les restriccions `@NotNull` dels camps temperature, humidity i pressure
- S'han canviat les columnes perquè siguin nullable a l'esquema de la base de dades

### 2. DTO SensorRequest (`src/main/java/com/iot/sensors/dto/SensorRequest.java`)
- S'han eliminat les validacions `@NotNull` dels camps de dades del sensor
- Nom i ubicació continuen sent obligatoris

### 3. Esquema de base de dades
Actualitzat per permetre valors NULL pels camps del sensor:
```sql
temperature float(53),        -- nullable
humidity float(53),           -- nullable
pressure float(53),           -- nullable
```

## Proves

### Proves manuals

1. Iniciar l'API:
```bash
mvn spring-boot:run
```

2. En una altra terminal, iniciar un simulador:
```bash
# Java
cd simulators/java && javac SensorSimulator.java && java SensorSimulator

# C
cd simulators/c && make && ./sensor_simulator
```

3. Verificar que les dades s'emmagatzemen:
```bash
curl http://localhost:8080/api/sensors
```

### Comportament esperat

La sortida del simulador hauria de mostrar:
- Informació del cicle
- Temps d'espera variant entre 20-40 segons
- Dades generades (1, 2 o 3 camps de manera aleatòria)
- Missatges ocasionals de pèrdua de paquets
- Estat d'èxit/fallada de les transmissions

Exemple:
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

## Qualitat del codi

### Revisió de codi
- ✅ Tots els comentaris de revisió abordats
- ✅ Registre coherent amb marques de temps
- ✅ Operacions de cadena eficients (sense crides repetides a strlen)
- ✅ Maneig d'errors adequat

### Seguretat
- ✅ Escaneig CodeQL superat (0 vulnerabilitats)
- ✅ Cap secret incrustat
- ✅ Validació d'entrada a l'API
- ✅ Gestió de memòria correcta en el codi C

### Proves
- ✅ Tots els tests unitari existents passen
- ✅ Simulador Java provat amb l'API en viu
- ✅ Dades parcials emmagatzemades correctament a la base de dades

## Diferències entre les versions Java i C

Ambdues implementacions ofereixen la mateixa funcionalitat, amb petites diferències:

| Aspecte | Java | C |
|--------|------|---|
| Llibreria HTTP | HttpURLConnection (inclòs) | libcurl (extern) |
| Compilació | Comanda única | Requereix libcurl-dev |
| Gestió memòria | Automàtica (GC) | Manual |
| Plataforma | Escriu una vegada, executa a qualsevol lloc | Compilació específica per plataforma |
| Rendiment | Bo | Ligerament millor |

## Configuració

Ambdós simuladors es poden personalitzar editant constants a la part superior dels fitxers font:

- `API_URL`: Endpoint de l'API destí
- `SENSOR_NAME`: Identificador del simulador
- `LOCATION`: Cadena d'ubicació
- `BASE_INTERVAL`: Temps base d'espera (30 segons)
- `TIME_VARIATION`: Variació aleatòria (±10 segons)

## Limitacions conegudes

1. **Simulador C**: Requereix que libcurl estigui instal·lat al sistema
2. **Sense autenticació**: Els simuladors assumeixen que l'API està oberta (apropiat per desenvolupament)
3. **Sensor únic**: Cada instància del simulador actua com un únic sensor
4. **Sense lògica de reintents**: Les transmissions fallides es registren però no es reintenten

## Millores futures

Millores potencials per als simuladors:

1. Arguments de línia d'ordres per a la configuració
2. Varios sensors concurrents des d'un mateix procés
3. Taxa de pèrdua de paquets configurable
4. Lògica de reintents per a transmissions fallides
5. Suport per HTTPS amb certificats
6. Registre a fitxer en lloc de la consola
7. Informe d'estadístiques (taxa d'èxit, temps mitjà, etc.)

## Conclusió

Ambdues versions del simulador implementen amb èxit els requisits i proporcionen una manera realista de provar l'API IoT Sensors amb patrons de dades i condicions de xarxa variables.

------

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
