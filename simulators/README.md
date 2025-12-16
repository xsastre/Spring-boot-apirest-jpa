# IoT Sensor Simulators

Programes de simulació de sensors IoT per al projecte Spring Boot API REST.

## Descripció

Aquest directori conté dos programes que simulen el comportament de sensors IoT:

1. **Java Simulator** - Versió implementada en Java
2. **C Simulator** - Versió implementada en C

Ambdós programes tenen el mateix comportament i funcionalitat.

## Característiques dels simuladors

Els simuladors implementen el següent comportament:

- **Temporització variable**: Envien dades aproximadament cada 30 segons amb una variació aleatòria de ±10 segons (entre 20 i 40 segons)
- **Dades ficticies**: Generen valors aleatoris de:
  - Temperatura: 15-30°C
  - Humitat: 30-80%
  - Pressió atmosfèrica: 980-1040 hPa
- **Combinacions aleatòries**: En cada transmissió envien 1, 2 o 3 dels paràmetres anteriors de forma aleatòria
- **Simulació de pèrdua de paquets**: Aproximadament el 10% de les vegades, després d'esperar el temps programat, no envien cap dada (simulant pèrdua de paquets en la transmissió)

## Estructura

```
simulators/
├── README.md          # Aquest fitxer
├── java/              # Simulador en Java
│   ├── SensorSimulator.java
│   └── README.md
└── c/                 # Simulador en C
    ├── sensor_simulator.c
    ├── Makefile
    └── README.md
```

## Ús

### Requisits previs

Abans d'executar qualsevol simulador, assegura't que l'API REST està en funcionament:

```bash
# Des del directori arrel del projecte
mvn spring-boot:run
```

L'API hauria d'estar accessible a `http://localhost:8080`

### Executar el simulador Java

```bash
cd java
javac SensorSimulator.java
java SensorSimulator
```

Consulta [java/README.md](java/README.md) per més detalls.

### Executar el simulador C

```bash
cd c
make
./sensor_simulator
```

Consulta [c/README.md](c/README.md) per més detalls.

## Exemple de funcionament

Quan un simulador s'executa, veuràs sortides com aquesta:

```
=== IoT Sensor Simulator (Java/C) ===
API URL: http://localhost:8080/api/sensors
Sensor: Sensor-Simulator-Java/C
Location: Test-Lab
Interval: ~30s (±10s)
Starting simulation...

[2025-01-15 10:30:00] Cycle 1 starting...
[2025-01-15 10:30:00] Waiting 28 seconds...
[2025-01-15 10:30:28] Generated data: Temperature=22.45°C, Humidity=65.32%
[2025-01-15 10:30:28] SUCCESS - Data sent to API

[2025-01-15 10:30:28] Cycle 2 starting...
[2025-01-15 10:30:28] Waiting 35 seconds...
[2025-01-15 10:31:03] PACKET LOSS - Skipping transmission

[2025-01-15 10:31:03] Cycle 3 starting...
[2025-01-15 10:31:03] Waiting 25 seconds...
[2025-01-15 10:31:28] Generated data: Pressure=1013.25 hPa
[2025-01-15 10:31:28] SUCCESS - Data sent to API
```

## Verificar les dades enviades

Pots verificar que les dades s'han rebut correctament consultant l'API:

```bash
# Obtenir totes les lectures de sensors
curl http://localhost:8080/api/sensors

# Accedir a Swagger UI
# Obre el navegador a: http://localhost:8080/swagger-ui.html
```

## Aturar els simuladors

Per aturar qualsevol simulador en execució, prem `Ctrl+C` al terminal.

## Personalització

Ambdós simuladors poden ser personalitzats modificant les constants al codi:

- `API_URL`: URL de destinació de l'API
- `SENSOR_NAME`: Nom del sensor
- `LOCATION`: Localització del sensor
- `BASE_INTERVAL`: Interval base de transmissió (30 segons)
- `TIME_VARIATION`: Variació de temps (±10 segons)

## Notes tècniques

### Versió Java
- Utilitza `HttpURLConnection` per enviar peticions HTTP
- No requereix biblioteques externes
- Compatible amb Java 17+

### Versió C
- Utilitza `libcurl` per enviar peticions HTTP
- Requereix instal·lar libcurl-dev
- Compatible amb GCC i altres compiladors C estàndard

## Resolució de problemes

### Error de connexió

Si els simuladors no poden connectar amb l'API:

1. Verifica que l'API està en execució: `curl http://localhost:8080/api/sensors`
2. Comprova que el port 8080 no està bloquejat per un firewall
3. Revisa els logs de l'aplicació Spring Boot

### Errors de compilació (C)

Si tens errors compilant la versió C:

1. Assegura't que libcurl està instal·lat: `curl-config --version`
2. Instal·la les dependencies de desenvolupament de libcurl (veure c/README.md)

## Llicència

Apache 2.0
