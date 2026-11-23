# Guía Paso a Paso: Arquitectura por Capas con JDBC, CDI (Weld SE) y DTOs

Este tutorial recoge la estructura completa, el orden de creación de clases y las reglas de diseño aplicadas para construir un proyecto CRUD en Java con acceso a datos en MySQL, inyección de dependencias (CDI) y control de transacciones.

---

## 1. Resumen Inicial: Orden de Creación de Clases y Capas

Para evitar errores de dependencias circulares y construir el proyecto de forma ordenada de abajo hacia arriba, sigue siempre esta secuencia de capas:

### Mapa de Capas y Reglas de Diseño
1. **`common` y `domain.exceptions`**:
   * Constantes generales (`Constants`) y consultas SQL parametrizadas (`SQLQueries`).
   * Excepciones de negocio y persistencia (`DatabaseError`, `DuplicatedUsernameError`, `ForeignKeyError`, `NotEnoughTokensError`).
2. **`dao` (Capa de Acceso a Datos)**:
   * `dao.model`: Entidades que reflejan exactamente las tablas de la base de datos (`Player`, `PlayerCharacter`, `Mission`, `CompletedMission`).
   * `dao.mappers`: `RowMapper` que transforman cada fila de un `ResultSet` de JDBC en objetos de `dao.model`.
   * `dao.interfaces`: Interfaces que definen el contrato de cada repositorio (`PlayerRepository`, `PlayerCharacterRepository`, `MissionRepository`, `CompletedMissionRepository`).
   * `dao.repositories.JDBC`: `DBConnectionPool` (`@Singleton` + `@PreDestroy`) e implementaciones JDBC con manejo explícito de transacciones (`commit` / `rollback`).
3. **`domain` (Capa de Lógica de Negocio)**:
   * `domain.model`: **DTOs** (`PlayerDTO`, `CharacterDTO`, `MissionDTO`, `CompletedMissionDTO`) diseñados para transportar datos hacia y desde la interfaz de usuario.
   * `domain.mappers`: Mappers (`dtoToEntity` y `entityToDto`) que traducen entre `dao.model` y `domain.model`.
   * `domain.services`: Servicios (`PlayerService`, `CharacterService`, `MissionService`) anotados con `@Inject` que validan reglas de negocio y coordinan repositorios y mappers.
4. **`ui` (Capa de Presentación)**:
   * Clases ejecutables (`Exercise1`, `Exercise2`, `Exercise3`, `Exercise4`) que inicializan el contenedor CDI (`SeContainer`), solicitan los servicios, interactúan con el usuario y capturan excepciones.
   * **Regla estricta:** La capa `ui` **nunca** importa clases de `dao` (`dao.model`, `dao.interfaces`, `dao.repositories`). Solo trabaja con `domain.services`, `domain.model` (DTOs) y `domain.exceptions`.

---

## 2. Desarrollo Paso a Paso por Capas

### Paso 1: Configuración de CDI (Weld SE) y Excepciones
Antes de crear las clases de negocio, configura el contenedor de inyección de dependencias y los errores controlados:
* Crea el archivo `src/main/resources/META-INF/beans.xml` con `bean-discovery-mode="all"` para que Weld detecte automáticamente todas las clases con constructores `@Inject`.
* Define las excepciones en `domain.exceptions` heredando de `RuntimeException` para poder propagar errores limpios desde el `DAO` o el `Service` hasta la `UI`.

---

### Paso 2: Capa `dao` (Pool, Modelos, RowMappers y Repositorios)

1. **Pool de Conexiones (`DBConnectionPool`):**
   * Se anota con `@Singleton` para mantener una única instancia de `HikariDataSource` durante todo el ciclo de vida de la aplicación.
   * Su método de cierre se anota con `@PreDestroy` para que Weld libere las conexiones automáticamente al cerrarse el bloque `try (SeContainer container = ...)`.

2. **Modelos DAO (`dao.model`) y RowMappers (`dao.mappers`):**
   * Cada entidad del DAO (`Player`, `PlayerCharacter`, `Mission`, `CompletedMission`) tiene un `RowMapper` asociado (`PlayerRowMapper`, `CharacterRowMapper`, `MissionRowMapper`, `CompletedMissionsRowMapper`) que extrae los valores del `ResultSet` y construye el objeto.

3. **Consultas Parametrizadas (`SQLQueries.java`):**
   * Centraliza todas las sentencias SQL usando parámetros `?` en `PreparedStatement` para evitar inyección SQL y facilitar su mantenimiento.

4. **Gestión de Transacciones JDBC:**
   * En operaciones compuestas que modifican varias tablas o insertan registros dependientes (padre e hijos), se desactiva el autoguardado con `con.setAutoCommit(false)`:
     * Si todas las sentencias finalizan correctamente $\rightarrow$ `con.commit()`.
     * Si ocurre cualquier excepción SQL o de integridad $\rightarrow$ `con.rollback()` en el bloque `catch`.
     * Finalmente, en el `finally` $\rightarrow$ `con.setAutoCommit(true)`.

---

### Paso 3: Capa `domain` (DTOs y MappersDTO)

1. **DTOs (`domain.model`):**
   * Representan los datos que necesita la vista sin exponer detalles internos de la base de datos.
   * Por ejemplo, `PlayerDTO` incluye una lista `List<CharacterDTO>` para poder enviar un jugador junto con todos sus personajes en una sola llamada desde la `UI`.

2. **Mappers DTO (`domain.mappers`):**
   * Cada entidad cuenta con su clase mapper (`PlayerDTOMapper`, `CharacterDTOMapper`, `MissionDTOMapper`, `CompletedMissionDTOMapper`) con dos métodos estándar:
     * `dtoToEntity(DTO dto)`: Convierte los datos recibidos desde la `UI` en entidades para pasarlas al `DAO`.
     * `entityToDto(Entity entity)`: Convierte las entidades devueltas por el `DAO` en DTOs seguros para la `UI`.

---

### Paso 4: Implementación de los Casos de Uso (Servicios + UI)

#### Caso 1: Inserción Transaccional de Entidad Padre con Hijos (`Exercise2`)
* **Objetivo:** Insertar un nuevo jugador (*Anabel*) junto con sus dos personajes (*Mr White* y *Mr Black*) de manera atómica.
* **DAO (`JDBCPlayerRepository.savePlayerWithCharacters`):**
  1. Inicia transacción (`con.setAutoCommit(false)`).
  2. Inserta el jugador usando `Statement.RETURN_GENERATED_KEYS`.
  3. Obtiene el `Player_ID` autogenerado mediante `ps.getGeneratedKeys()`.
  4. Recorre la lista de `PlayerCharacter`, asigna a cada uno el `Player_ID` recién creado y los inserta.
  5. Confirma con `con.commit()` (o revierte todo con `con.rollback()` si falla alguno).
* **Service (`PlayerService`):** Recibe `PlayerDTO`, convierte tanto el jugador como su lista de `CharacterDTO` mediante los mappers y delega en `playerRepository.savePlayerWithCharacters(player, characters)`.
* **UI (`Exercise2`):** Crea los objetos `CharacterDTO` y `PlayerDTO`, llama a `playerService.savePlayerWithCharacters(anabel)` y muestra el ID generado.

#### Caso 2: Borrado Condicional con Confirmación del Usuario (`Exercise3`)
* **Objetivo:** Eliminar el personaje *Mr White*. Si tiene misiones completadas registradas, pedir confirmación al usuario antes de borrarlo junto con su historial.
* **DAO (`JDBCCharacterRepository.deleteCharacter`):**
  * Dentro de una transacción (`setAutoCommit(false)`), si `confirmation` es `true`, elimina primero los registros asociados en `completed_missions` y después elimina el personaje en `Characters`.
* **Service (`CharacterService`):**
  1. Busca el personaje por nombre (`getByName`). Si no existe, lanza `DatabaseError`.
  2. Consulta sus misiones completadas (`getAllByCharacterName`).
  3. Si `!missions.isEmpty() && !confirmation`, lanza `ForeignKeyError` para detener el borrado y avisar a la capa superior.
  4. Si no tiene misiones o ya existe confirmación, llama a `characterRepository.deleteCharacter(id, confirmation)`.
* **UI (`Exercise3`):**
  1. Llama inicialmente a `characterService.deleteCharacter("Mr White", false)`.
  2. Captura `ForeignKeyError`, solicita confirmación por consola mediante `Scanner` y, si el usuario responde `"yes"`, repite la llamada con `confirmation = true`.

#### Caso 3: Validación de Reglas de Negocio y Actualización (`Exercise4`)
* **Objetivo:** Registrar que *Anabel* ha completado la misión *Dragon Hunt* en la fecha actual con *Mr White*, descontando los tokens correspondientes o mostrando un error si no tiene suficientes.
* **Service (`MissionService`):**
  1. Recupera `Player`, `PlayerCharacter` y `Mission` por nombre, comprobando que existan y que el personaje pertenezca al jugador.
  2. Valida la regla de negocio: si `player.getTokens() < mission.getTokens()`, lanza `NotEnoughTokensError` antes de realizar ninguna escritura.
  3. Calcula los tokens restantes (`player.getTokens() - mission.getTokens()`).
  4. Registra la misión completada con `LocalDate.now()` (`saveCompletedMission`) y actualiza el saldo de tokens del jugador (`updateTokens`).
* **UI (`Exercise4`):** Invoca `missionService.completeMission("Anabel", "Mr White", "Dragon Hunt", LocalDate.now())` y captura `NotEnoughTokensError | DatabaseError` para informar por consola.

---

## 3. Checklist Rápido de Verificación

- [ ] **Aislamiento de capas:** Ninguna clase dentro del paquete `ui` contiene `import dao.*`.
- [ ] **Inyección de dependencias:** Repositorios, mappers y servicios se inyectan por constructor con `@Inject` (sin instanciarlos manualmente con `new` en la `UI`).
- [ ] **Cierre de recursos:** Todos los `Connection`, `PreparedStatement` y `ResultSet` están gestionados mediante `try-with-resources`.
- [ ] **Atomicidad en BD:** Las operaciones compuestas desactivan `autoCommit`, confirman con `commit()` y revierten con `rollback()` ante errores.
- [ ] **Excepciones limpias:** Los fallos de SQL y las reglas de negocio se comunican mediante excepciones personalizadas del dominio.