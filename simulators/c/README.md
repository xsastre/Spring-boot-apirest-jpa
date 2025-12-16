# C Sensor Simulator

Programa de simulació de sensors IoT en C.

## Característiques

Aquest programa simula el comportament d'un sensor IoT amb les següents característiques:

- **Temporització aleatòria**: Envia dades cada ~30 segons amb una variació de ±10 segons
- **Dades aleatòries**: Genera valors ficticis de temperatura (15-30°C), humitat (30-80%), i pressió atmosfèrica (980-1040 hPa)
- **Combinacions variables**: Envia 1, 2 o 3 paràmetres de forma aleatòria
- **Pèrdua de paquets**: Simula pèrdua de transmissió (10% de probabilitat de no enviar dades)

## Requisits

- GCC o qualsevol compilador C compatible
- libcurl (biblioteca per fer peticions HTTP)
- API REST en execució a `http://localhost:8080`

### Instal·lar libcurl

**Ubuntu/Debian:**
```bash
sudo apt-get update
sudo apt-get install libcurl4-openssl-dev
```

**macOS:**
```bash
brew install curl
```

**Fedora/RHEL:**
```bash
sudo dnf install libcurl-devel
```

## Compilació

### Amb Makefile (recomanat)

```bash
make
```

### Compilació manual

```bash
gcc -Wall -Wextra -O2 -o sensor_simulator sensor_simulator.c -lcurl
```

## Execució

```bash
./sensor_simulator
```

O amb Makefile:

```bash
make run
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
=== IoT Sensor Simulator (C) ===
API URL: http://localhost:8080/api/sensors
Sensor: Sensor-Simulator-C
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
[2025-01-15 10:31:28] Generated data: Humidity=55.21%
[2025-01-15 10:31:28] SUCCESS - Data sent to API
```

## Configuració

Pots modificar les constants al principi del codi (`sensor_simulator.c`):

- `API_URL`: URL de l'API
- `SENSOR_NAME`: Nom del sensor
- `LOCATION`: Localització del sensor
- `BASE_INTERVAL`: Interval base en segons (30)
- `TIME_VARIATION`: Variació de temps en segons (±10)

## Neteja

Per eliminar el fitxer compilat:

```bash
make clean
```

## Aturar el programa

Prem `Ctrl+C` per aturar l'execució.

## Notes

- El programa utilitza libcurl per fer peticions HTTP POST a l'API
- El programa es repeteix indefinidament fins que es atura manualment
- Els valors generats són completament aleatoris dins dels rangs especificats
