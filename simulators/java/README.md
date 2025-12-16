# Java Sensor Simulator

Programa de simulació de sensors IoT en Java.

## Característiques

Aquest programa simula el comportament d'un sensor IoT amb les següents característiques:

- **Temporització aleatòria**: Envia dades cada ~30 segons amb una variació de ±10 segons
- **Dades aleatòries**: Genera valors ficticis de temperatura (15-30°C), humitat (30-80%), i pressió atmosfèrica (980-1040 hPa)
- **Combinacions variables**: Envia 1, 2 o 3 paràmetres de forma aleatòria
- **Pèrdua de paquets**: Simula pèrdua de transmissió (10% de probabilitat de no enviar dades)

## Requisits

- Java 17 o superior
- API REST en execució a `http://localhost:8080`

## Compilació

```bash
javac SensorSimulator.java
```

## Execució

```bash
java SensorSimulator
```

## Funcionament

El programa fa el següent:

1. Espera un temps aleatori entre 20 i 40 segons (30 ± 10)
2. Amb 10% de probabilitat, simula pèrdua de paquets i no envia res
3. Genera dades aleatòries de 1, 2 o 3 camps:
   - Temperatura (°C)
   - Humitat (%)
   - Pressió atmosfèrica (hPa)
4. Envia les dades a l'API via POST a `/api/sensors`
5. Mostra el resultat de la transmissió
6. Torna al pas 1

## Exemple de sortida

```
=== IoT Sensor Simulator (Java) ===
API URL: http://localhost:8080/api/sensors
Sensor: Sensor-Simulator-Java
Location: Test-Lab
Interval: ~30s (±10s)
Starting simulation...

[2025-01-15 10:30:00] Cycle 1 starting...
[2025-01-15 10:30:00] Waiting 28 seconds...
[2025-01-15 10:30:28] Generated data: Temperature=22.45°C, Humidity=65.32%, Pressure=1013.25 hPa
[2025-01-15 10:30:28] SUCCESS - Data sent to API

[2025-01-15 10:30:28] Cycle 2 starting...
[2025-01-15 10:30:28] Waiting 35 seconds...
[2025-01-15 10:31:03] PACKET LOSS - Skipping transmission

[2025-01-15 10:31:03] Cycle 3 starting...
[2025-01-15 10:31:03] Waiting 25 seconds...
[2025-01-15 10:31:28] Generated data: Temperature=18.76°C
[2025-01-15 10:31:28] SUCCESS - Data sent to API
```

## Configuració

Pots modificar les constants al principi del codi:

- `API_URL`: URL de l'API
- `SENSOR_NAME`: Nom del sensor
- `LOCATION`: Localització del sensor
- `BASE_INTERVAL`: Interval base en segons (30)
- `TIME_VARIATION`: Variació de temps en segons (±10)

## Aturar el programa

Prem `Ctrl+C` per aturar l'execució.
