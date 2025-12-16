# Resum d'Implementació - API REST per Sensors IoT

English version available at: [IMPLEMENTATION_SUMMARY.md](./IMPLEMENTATION_SUMMARY.md)

## Visió general
Una API REST completa i funcional amb Spring Boot 4.0.0 per gestionar lectures de sensors IoT (temperatura, humitat i pressió) amb documentació exhaustiva i suport per a dos entorns.

## Requisits complerts

### ✅ Spring Boot 4.0.0
- Implementat amb Spring Boot 4.0.0 i Java 17
- Sistema de construcció Maven configurat

### ✅ API REST per sensors IoT
S'han implementat operacions CRUD completes per a les dades dels sensors:
- **Temperatura**: Valor numèric per lectures de temperatura (opcional)
- **Humitat**: Valor numèric per percentatge d'humitat (opcional)
- **Pressió**: Valor numèric per la pressió atmosfèrica (opcional)
- Suporta dades parcials del sensor (qualsevol combinació d'1, 2 o 3 camps)

### ✅ Simuladors de sensors
S'han implementat simuladors de sensors per provar l'API:
- **Simulador Java**: Programa Java autònom que simula el comportament d'un sensor IoT
- **Simulador C**: Programa en C que utilitza libcurl per a peticions HTTP
- Ambdós simuladors inclouen:
  - Temporització aleatòria: ~30 segons ± 10 segons entre transmissions
  - Generació de dades fictícies: Temperatura (15-30°C), Humitat (30-80%), Pressió (980-1040 hPa)
  - Selecció aleatòria de camps: Envia 1, 2 o 3 camps aleatòriament
  - Simulació de pèrdua de paquets: 10% de probabilitat de saltar una transmissió

### ✅ Documentació Swagger
- Integrat SpringDoc OpenAPI 3 (versió 2.7.0)
- Disponible a: `/swagger-ui.html`
- Documentació de l'API a: `/api-docs`
- Descripcions d'endpoint detallades i anotacions

### ✅ Dos entorns

#### Entorn de desenvolupament
- Base de dades: H2 en memòria
- Creació automàtica de l'esquema a l'arrencada
- Dades de mostra pre-carregades
- Consola H2 activada a `/h2-console`
- Perfil: `dev` (per defecte)

#### Entorn de producció
- Base de dades: PostgreSQL
- Gestió manual de l'esquema amb scripts SQL
- Pool de connexions configurat (HikariCP)
- Perfil: `prod`

### ✅ Scripts SQL per a la creació de la base de dades
S'han creat scripts SQL generals:
- `schema.sql`: Esquema genèric per a ambdós entorns
- `schema-postgresql.sql`: Script específic per PostgreSQL amb índexs
- `data-dev.sql`: Dades d'exemple per a l'entorn de desenvolupament

## Tècnica d'accés a la base de dades

### JPA (Java Persistence API) amb Spring Data JPA

Aquest projecte implementa l'estàndard **JPA**, utilitzant **Spring Data JPA** per simplificar les implementacions de repositoris.

#### Detalls d'implementació:

1. **Model d'entitat JPA**
   - Classe d'entitat `Sensor` anotada amb `@Entity`
   - Anotacions de Jakarta Persistence API per al mapeig ORM
   - Generació automàtica de la clau primària amb `@GeneratedValue(strategy = GenerationType.IDENTITY)`
   - Callbacks de cicle de vida amb `@PrePersist` i `@PreUpdate`
   - Mapeig de columnes amb restriccions adequades

2. **Patró de repositori Spring Data JPA**
   - `SensorRepository` estèn `JpaRepository<Sensor, Long>`
   - No requereix codi d'implementació: Spring ho genera en temps d'execució
   - Mètodes CRUD integrats: `save()`, `findById()`, `findAll()`, `deleteById()`
   - Mètodes de consulta derivats personalitzats: `findByLocation()`, `findByName()`
   - Gestió de transaccions automàtica

3. **Hibernate com a proveïdor JPA**
   - Hibernate 6.x (inclòs amb Spring Boot 4.0.0)
   - Generació automàtica de DDL a l'entorn de desenvolupament
   - HikariCP per al pool de connexions
   - Suport per a múltiples dialectes de base de dades (H2, PostgreSQL)

4. **Beneficis d'aquest enfocament**
   - Operacions a la base de dades tipades (type-safe)
   - Independència del proveïdor de base de dades
   - Reducció de codi repetitiu
   - Mapeig automàtic d'entitats a taules
   - Gestió de transaccions integrada
   - Fàcil de provar amb bases de dades en memòria

## Arquitectura

### Arquitectura per capes
1. **Capa de Controlador**: Endpoints REST amb anotacions Swagger
2. **Capa de Servei**: Lògica de negoci i gestió de transaccions
3. **Capa de Repositori**: Accés a dades JPA utilitzant repositoris Spring Data
4. **Capa de Model**: Entitats JPA amb anotacions ORM
5. **Capa DTO**: Objectes de transferència de dades (request/response)
6. **Capa d'Excepcions**: Gestió d'excepcions personalitzada

### Components clau

#### Entitat
- **Sensor**: Entitat principal amb camps per nom, ubicació, temperatura, humitat, pressió i marques de temps

#### Endpoints REST
- `POST /api/sensors` - Crear una nova lectura del sensor
- `GET /api/sensors` - Obtenir totes les lectures dels sensors
- `GET /api/sensors/{id}` - Obtenir sensor per ID
- `GET /api/sensors/location/{location}` - Obtenir sensors per ubicació
- `GET /api/sensors/name/{name}` - Obtenir sensors per nom
- `PUT /api/sensors/{id}` - Actualitzar una lectura del sensor
- `DELETE /api/sensors/{id}` - Eliminar una lectura del sensor

#### Gestió d'excepcions
- `SensorNotFoundException` personalitzada per errors 404
- `GlobalExceptionHandler` per a la gestió centralitzada d'errors
- Respostes d'error de validació amb detalls per camp

#### Validació
- Validació d'entrada amb Jakarta Bean Validation
- Validació de camps obligatoris
- Codis d'estat HTTP adequats (201, 200, 404, 400)

## Proves

### Tests unitari
- El context de l'aplicació s'inicia correctament
- Tots els tests passen

### Proves manuals
- S'han verificat totes les operacions CRUD
- S'han provat la gestió d'errors (404, errors de validació)
- Swagger UI accessible i funcional
- Dades de mostra carregades correctament en desenvolupament

## Seguretat

### Anàlisi CodeQL
- ✅ Cap vulnerabilitat de seguretat trobada
- Tot el codi revisat i validat

## Estructura del projecte

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

## Com executar

### Mode desenvolupament (H2)
```bash
mvn spring-boot:run
# o
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Mode producció (PostgreSQL)
```bash
# Primer, crear la base de dades i executar l'esquema
psql -U postgres -d iot_sensors_db -f src/main/resources/schema-postgresql.sql

# Després executar l'aplicació
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Exemples d'API

### Crear lectura de sensor (tots els camps)
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

### Crear lectura de sensor (dades parcials)
```bash
curl -X POST http://localhost:8080/api/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sensor-002",
    "location": "Laboratory",
    "temperature": 22.3
  }'
```

### Obtenir tots els sensors
```bash
curl http://localhost:8080/api/sensors
```

### Executar el simulador de sensor (Java)
```bash
cd simulators/java
javac SensorSimulator.java
java SensorSimulator
```

### Executar el simulador de sensor (C)
```bash
cd simulators/c
make
./sensor_simulator
```

### Exemple de resposta d'error
```bash
# Sensor no existent (404)
curl http://localhost:8080/api/sensors/999

# Resposta:
{
  "status": 404,
  "message": "Sensor not found with id: 999",
  "timestamp": "2025-12-16T06:35:35.155513653"
}
```

## Característiques implementades

✅ Operacions CRUD completes  
✅ Suport per dades parcials dels sensors (camps opcionals)  
✅ Validació d'entrada amb missatges d'error detallats  
✅ Gestió d'excepcions personalitzada  
✅ Documentació Swagger/OpenAPI  
✅ Suport per múltiples entorns  
✅ Scripts d'inicialització de la base de dades  
✅ Dades d'exemple per desenvolupament  
✅ Disseny RESTful de l'API  
✅ Codis d'estat HTTP apropiats  
✅ Gestió de transaccions  
✅ Pool de connexions  
✅ Simuladors de sensors (Java i C)  
✅ README i documentació exhaustiva

## Assegurament de qualitat

- ✅ El codi es compila correctament
- ✅ Tots els tests passen
- ✅ L'aplicació funciona correctament en mode desenvolupament
- ✅ Tots els endpoints de l'API provats i verificats
- ✅ Gestió d'errors validada
- ✅ Escaneig de seguretat superat (0 vulnerabilitats)
- ✅ Comentaris i feedback de revisió incorporats

## Documentació

- ✅ README exhaustiu amb exemples d'ús
- ✅ Swagger UI per a proves interactives de l'API
- ✅ Scripts SQL amb comentaris
- ✅ Comentaris al codi quan és oportú
- ✅ Resum d'implementació (aquest document)

## Conclusió

El projecte està complet i llest per producció. S'han complert tots els requisits del plantejament:
- Spring Boot 4.0.0 ✓
- API REST per sensors IoT (temperatura, humitat, pressió) ✓
- Suport per dades parcials dels sensors ✓
- Simuladors de sensors en Java i C ✓
- Documentació Swagger ✓
- Dos entorns (desenvolupament amb H2, producció amb PostgreSQL) ✓
- Scripts SQL per a la creació de la base de dades ✓

### Detalls d'implementació del simulador de sensors

Els simuladors de sensors implementen el comportament descrit:
1. **Temporització**: Envia dades cada ~30 segons amb variació aleatòria de ±10 segons (rang 20-40 segons)
2. **Generació de dades**: Genera valors ficticis per a temperatura, humitat i pressió
3. **Camps variables**: Envia aleatòriament 1, 2 o 3 dels paràmetres del sensor
4. **Pèrdua de paquets**: Simula fallades de transmissió (~10% dels cicles)

Les versions Java i C ofereixen funcionalitat idèntica i es poden utilitzar de manera interchangeable per provar l'API.

L'aplicació està llesta per producció i segueix les bones pràctiques de Spring Boot.
