# Guía de replicación del proyecto Hospital

Esta guía explica cómo reconstruir el proyecto desde cero y cómo justificar cada
parte en un examen de Acceso a Datos. La aplicación es un CRUD de consola para
pacientes, médicos y registros médicos, conectado a MySQL mediante JDBC y
gestionado con CDI/Weld.

> **Seguridad:** el archivo `src/main/resources/mysql-properties.xml` contiene
> la configuración de conexión del entorno original. No copies credenciales
> reales a un repositorio ni a la respuesta del examen. Usa tus propios valores,
> variables de entorno o un archivo local ignorado por Git.

## 1. Qué hace la aplicación

Al arrancar:

1. Se inicia el contenedor CDI de Weld.
2. CDI crea las clases de interfaz, servicios y repositorios mediante
   inyección de dependencias.
3. Se solicita `username` y `password`.
4. Se consulta `user_login` con una consulta parametrizada.
5. Si la contraseña coincide, se muestra un menú de consola.
6. El usuario puede consultar y modificar pacientes, médicos y registros
   médicos, además de borrar y restaurar pacientes.
7. Al elegir `11`, finaliza el bucle y se cierra el pool de conexiones.

El punto de entrada es `ui.Main`.

## 2. Tecnologías y dependencias

- Java 25, configurado en `pom.xml`.
- Maven.
- MySQL.
- JDBC con `mysql-connector-j`.
- Pool de conexiones HikariCP.
- CDI/Jakarta y Weld SE.
- Lombok para constructores, getters, setters y `toString`.
- XML DOM para almacenar copias de pacientes eliminados.

Las dependencias principales se declaran en `pom.xml`. Para reproducirlo en un
examen, basta crear un proyecto Maven y añadir esas dependencias con versiones
compatibles con el JDK disponible.

## 3. Estructura de carpetas

```text
src/main/java/
├── common/
│   ├── Configuration.java       # Lee la configuración XML
│   └── Constants.java           # Mensajes y consultas de usuario
├── dao/
│   ├── interfaces/              # Contratos de los repositorios
│   ├── mapper/                  # ResultSet -> entidad
│   ├── model/                   # Entidades de persistencia
│   └── repositories/             # Implementación JDBC
├── domain/
│   ├── exceptions/              # Excepciones de negocio
│   ├── mappers/                 # Entidad <-> DTO
│   ├── model/                   # DTO y datos del backup
│   └── service/                 # Reglas de negocio
└── ui/                          # Lectura, escritura y menú de consola

src/main/resources/
├── META-INF/beans.xml            # Activa el descubrimiento CDI
└── mysql-properties.xml          # URL, usuario y contraseña de MySQL

backups/
└── deleted-patients.xml         # Backup de pacientes eliminados
```

La dependencia entre capas debe quedar así:

```text
Main/UI -> Service -> Repository (interfaz) -> JDBC Repository -> MySQL
              |              |                    |
              DTO         Entidad             ResultSet Mapper
```

La UI no debería ejecutar SQL. El repositorio no debería pedir datos al
usuario. El servicio valida y coordina; los mappers convierten objetos.

## 4. Configuración de Maven

El `pom.xml` mínimo debe incluir:

```xml
<properties>
    <maven.compiler.source>25</maven.compiler.source>
    <maven.compiler.target>25</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

Dependencias necesarias:

| Dependencia | Uso |
|---|---|
| `org.projectlombok:lombok` | Código repetitivo de los modelos |
| `org.jboss.weld.se:weld-se-core` | Contenedor CDI en una aplicación Java SE |
| `jakarta.platform:jakarta.jakartaee-web-api` | Anotaciones e interfaces Jakarta |
| `com.mysql:mysql-connector-j` | Driver JDBC de MySQL |
| `com.zaxxer:HikariCP` | Pool de conexiones |

Si el examen utiliza Java 17 o 21, cambia `source` y `target` a esa versión y
usa versiones de dependencias compatibles. No mezcles `javax.*` con
`jakarta.*`: este proyecto utiliza Jakarta.

## 5. Base de datos

El código utiliza, como mínimo, estas tablas:

```text
patients
├── patient_id (PK)
├── name
├── date_of_birth
└── phone

user_login
├── id (PK)
├── username (UNIQUE)
├── password
└── patient_id (FK -> patients.patient_id)

doctors
├── doctor_id (PK)
├── name
├── specialization
└── phone

medical_records
├── record_id (PK)
├── patient_id (FK -> patients.patient_id)
├── doctor_id (FK -> doctors.doctor_id)
├── diagnosis
└── admission_date
```

Para el borrado completo de un paciente también se contemplan:

- `prescribed_medications`, relacionada mediante `record_id`.
- `appointments`, relacionada mediante `patient_id`.
- `patient_payments`, relacionada mediante `patient_id`.

Las claves primarias pueden ser `AUTO_INCREMENT`. `user_login.username` debe
ser único para que la aplicación pueda convertir una violación de integridad
en `DuplicatedUsernameException`.

Ejemplo de configuración local:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
    <entry key="urlDB">jdbc:mysql://localhost:3306/hospital</entry>
    <entry key="user_name">hospital_user</entry>
    <entry key="password">CAMBIAR_ESTA_CLAVE</entry>
</properties>
```

`common.Configuration` carga este XML con `Properties.loadFromXML`. La ruta
esperada durante la ejecución es `src/main/resources/mysql-properties.xml`;
Maven lo copia a `target/classes`.

## 6. Configuración del pool JDBC

`dao.repositories.DBConnection` recibe `Configuration` por CDI y crea un
`HikariDataSource`:

1. Lee URL, usuario y contraseña.
2. Fija un tamaño máximo de cuatro conexiones.
3. Activa la caché de sentencias preparadas.
4. Expone `getConnection()`.
5. Devuelve cada conexión mediante `try-with-resources`.
6. Cierra el pool en `@PreDestroy`.

El patrón importante es:

```java
try (Connection connection = db.getConnection();
     PreparedStatement statement = connection.prepareStatement(sql)) {
    statement.setInt(1, id);
    statement.executeUpdate();
}
```

Usa siempre `PreparedStatement` para los valores recibidos por el usuario.
Solo se construyen dinámicamente nombres de tabla/columna en la restauración,
y allí deben validarse contra una lista blanca.

## 7. Modelos, DTO y mappers

### Entidades DAO

- `Patient`: id, nombre, fecha de nacimiento, teléfono y credencial.
- `Doctor`: id, nombre, especialidad y teléfono.
- `MedicalRecords`: id, paciente, médico, diagnóstico y fecha de ingreso.
- `UserLogin`: id, usuario, contraseña y referencias a paciente/médico.

Son clases próximas a la base de datos y utilizan Lombok.

### DTO de dominio

- `PatientDTO`: datos mostrados y editados por la aplicación.
- `DoctorDTO`: información que se presenta del médico.
- `MedicalRecordDTO`: usa `String date` y `String description` para facilitar
  la entrada por consola.
- `CredentialDTO`: usuario y contraseña.

Los mappers aíslan las diferencias:

- `PatientDTOMapper`: `PatientDTO <-> Patient`.
- `DoctorDTOMapper`: `DoctorDTO <-> Doctor`.
- `MedicalRecordDTOMapper`: convierte `String` a `LocalDate` con
  `LocalDate.parse`.

Si se usa el formato ISO `yyyy-MM-dd`, `LocalDate.parse` funciona directamente.
Si el examen pide otro formato, hay que usar un `DateTimeFormatter` tanto al
leer como al guardar.

## 8. Repositorios JDBC

Cada repositorio implementa una interfaz de `dao.interfaces` y recibe
`DBConnection` y su mapper por constructor.

### Pacientes

`JDBCPatientRepository` implementa:

- `getAll`: `SELECT * FROM patients`.
- `add`: inserta paciente y credencial dentro de una transacción.
- `update`: actualiza nombre, fecha y teléfono.
- `delete`: elimina credenciales y paciente; si se confirma, elimina antes
  registros médicos, medicación, citas y pagos.
- `createBackup`: lee todas las filas relacionadas.
- `restore`: inserta el backup en orden de dependencias.

El alta es una transacción porque hay dos operaciones relacionadas:

```text
BEGIN
  INSERT patients
  leer generated key
  INSERT user_login con patient_id
COMMIT
```

Ante cualquier error se hace `ROLLBACK`. La clave generada se obtiene con
`Statement.RETURN_GENERATED_KEYS`.

### Médicos

`JDBCDoctorRepository` ejecuta `SELECT * FROM doctors`, usa
`doctorQueryMapper` y devuelve una lista de `Doctor`.

### Registros médicos

`JDBCMedicalRecordsRepository` permite:

- listar por `patient_id`;
- insertar y devolver `record_id`;
- actualizar por `record_id`;
- borrar por `record_id`.

Las fechas de `LocalDate` se convierten con `Date.valueOf` y las fechas de
MySQL se convierten con `ResultSet.getDate(...).toLocalDate()`.

### Credenciales

`JDBCCredentialRepository` consulta solo la contraseña:

```sql
SELECT password FROM user_login WHERE username = ?
```

El servicio compara la contraseña introducida con la recuperada. Para una
aplicación real se debe almacenar un hash, no la contraseña en texto plano; si
el examen lo permite, explica esta mejora aunque mantengas el contrato actual.

## 9. Servicios y validaciones

Los servicios reciben repositorios y mappers mediante constructor injection.

### `PatientService`

- convierte entidades a DTO al listar;
- valida paciente no nulo;
- exige nombre no vacío;
- exige fecha de nacimiento;
- valida usuario y contraseña al crear;
- coordina alta, modificación, borrado y restauración.

### `CredentialService`

Rechaza credenciales vacías, recupera el usuario y compara la contraseña.

### `DoctorService` y `MedicalRecordsService`

Delegan las operaciones a sus repositorios y convierten entre DTO y entidad.

### `PatientBackupService`

Gestiona `backups/deleted-patients.xml`:

1. Crea la carpeta si no existe.
2. Lee las filas de `patients`, `medical_records` y tablas relacionadas.
3. Guarda los tipos JDBC y los valores en XML.
4. Al restaurar, reconstruye las filas en orden:
   `patients`, `medical_records`, `prescribed_medications`,
   `appointments`, `patient_payments`.
5. Elimina la copia después de restaurarla.

La restauración se hace en transacción. Si falla, se revierte todo. Las tablas
válidas están limitadas por `BACKUP_TABLES`, evitando insertar datos en una
tabla arbitraria.

## 10. CDI y arranque

`src/main/resources/META-INF/beans.xml` usa `bean-discovery-mode="all"`.
Las clases que CDI debe crear llevan `@Inject` en el constructor; la
configuración lleva `@Singleton`.

El arranque esencial es:

```java
SeContainerInitializer initializer = SeContainerInitializer.newInstance();
SeContainer container = initializer.initialize();
PatientUI patientUI = container.select(PatientUI.class).get();
```

En un examen, recuerda cerrar el contenedor con `try-with-resources` si no
estás siguiendo exactamente la implementación existente:

```java
try (SeContainer container =
         SeContainerInitializer.newInstance().initialize()) {
    // ejecutar la aplicación
}
```

## 11. Interfaz de consola y flujo CRUD

`ui.Main` muestra el menú y delega en las clases UI:

| Opción | Operación |
|---:|---|
| 1 | Listar pacientes |
| 2 | Añadir paciente y credencial |
| 3 | Actualizar paciente |
| 4 | Borrar paciente |
| 5 | Listar médicos |
| 6 | Listar registros de un paciente |
| 7 | Añadir registro médico |
| 8 | Actualizar registro médico |
| 9 | Borrar registro médico |
| 10 | Restaurar paciente desde XML |
| 11 | Salir |

Las clases UI leen con `Scanner`, llaman a un servicio y muestran mensajes.
La UI de pacientes tiene un caso especial:

1. Intenta borrar el paciente sin borrar relaciones.
2. Si MySQL lanza una violación de clave foránea, pregunta al usuario.
3. Con `Y`, crea el backup y repite el borrado con confirmación.
4. Con cualquier otra respuesta, cancela.

## 12. Excepciones

Las excepciones de dominio se encuentran en `domain.exceptions`:

- `DatabaseError`: error de acceso a datos.
- `DuplicatedUsernameException`: usuario duplicado.
- `ForeignKeyException`: el paciente todavía tiene relaciones.
- `BackupNotFoundException`: no existe copia para restaurar.
- `AppException`: error general presentado por la UI.

En JDBC, captura `SQLException`, registra el detalle técnico y lanza una
excepción de dominio con un mensaje entendible para la capa superior. No
devuelvas `null` como sustituto silencioso de un error salvo que ese sea el
contrato explícito, como ocurre con una credencial inexistente.

## 13. Cómo reproducirlo paso a paso en un examen

1. **Crear el proyecto Maven** y configurar el JDK.
2. **Añadir dependencias** de Lombok, Weld, Jakarta, MySQL y HikariCP.
3. **Crear `beans.xml`** en `src/main/resources/META-INF`.
4. **Crear el XML de propiedades** con una base de datos local.
5. **Crear el esquema MySQL** con claves primarias, foráneas y usuario único.
6. **Definir entidades DAO** con los tipos de la base de datos.
7. **Definir interfaces de repositorio** antes de escribir JDBC.
8. **Crear `DBConnection`** y probar que devuelve una conexión.
9. **Crear constantes SQL** y repositorios con `PreparedStatement`.
10. **Crear mappers de `ResultSet`** para no mezclar JDBC con servicios.
11. **Crear DTO y mappers de dominio**.
12. **Crear servicios** con validaciones y transacciones coordinadas por el DAO.
13. **Crear UI y `Main`** con el menú.
14. **Añadir backup XML** si el enunciado exige borrado/restauración.
15. **Probar primero login**, después lecturas y finalmente operaciones que
    modifican datos.

Una estrategia recomendable es implementar en este orden: listar pacientes,
login, alta, modificación, borrado, médicos, registros y finalmente backup.
Así cada capa se puede probar antes de añadir la siguiente.

## 14. Comandos de compilación y ejecución

Desde la carpeta del proyecto:

```powershell
mvn clean compile
```

Para ejecutar una clase Java desde IntelliJ, selecciona `ui.Main` y configura
el classpath Maven. Si se ejecuta manualmente con `java`, hay que incluir las
dependencias transitivas y procesar correctamente CDI; por eso IntelliJ o un
plugin Maven de ejecución suele ser la opción más sencilla.

Antes de ejecutar, comprueba:

- que el servidor MySQL responde;
- que la URL incluye host, puerto y base de datos correctos;
- que existen las tablas y datos de prueba;
- que `mysql-properties.xml` está dentro de `src/main/resources`;
- que el JDK seleccionado coincide con el `pom.xml`;
- que Lombok está habilitado en el IDE.

## 15. Pruebas que conviene demostrar

| Prueba | Resultado esperado |
|---|---|
| Login correcto | Entra al menú |
| Login con usuario inexistente | Muestra login fallido |
| Alta sin nombre | Rechaza el paciente |
| Alta sin fecha | Rechaza el paciente |
| Alta con username repetido | Hace rollback y avisa |
| Listado por paciente | Devuelve solo sus registros |
| Borrado con relaciones | Pide confirmación |
| Cancelación del borrado | No elimina datos |
| Borrado confirmado | Guarda XML y elimina relaciones |
| Restauración existente | Inserta datos y elimina la copia |
| Restauración inexistente | Muestra backup no encontrado |

## 16. Errores típicos y cómo explicarlos

- **`NullPointerException` al conectar:** revisar la ruta del XML y las claves
  `urlDB`, `user_name` y `password`.
- **`No suitable driver`:** falta `mysql-connector-j` en Maven.
- **Error de CDI:** falta `beans.xml`, una clase no es descubrible o hay una
  dependencia sin constructor inyectable.
- **Fecha inválida:** la entrada debe seguir `yyyy-MM-dd`.
- **Usuario duplicado:** debe existir una restricción `UNIQUE` y el alta debe
  usar `rollback`.
- **Borrado bloqueado:** primero se deben eliminar las filas hijas o pedir
  confirmación para el borrado completo.
- **Backup que no restaura:** hay que insertar respetando el orden de las
  claves foráneas y conservar el tipo JDBC de cada valor.
- **Recursos abiertos:** usar `try-with-resources` para conexión, sentencia y
  `ResultSet`.

## 17. Mejoras que se pueden mencionar

Si el profesor pide valorar el diseño, se pueden proponer estas mejoras:

1. Guardar contraseñas con hash y salt.
2. Sacar las credenciales de Git mediante variables de entorno.
3. Añadir tests unitarios para servicios y tests de integración para JDBC.
4. Validar también identificadores, teléfonos y existencia de claves foráneas.
5. Usar un único `Scanner` compartido en vez de crear uno por cada lectura.
6. Cerrar explícitamente el `SeContainer` en `Main`.
7. Añadir `maven-compiler-plugin` y una configuración de ejecución Maven.
8. Centralizar el manejo de errores para no registrar y ocultar excepciones de
   forma inconsistente.

La idea clave para defender el proyecto es separar responsabilidades: la UI
recoge datos, el servicio valida y coordina, el repositorio ejecuta SQL, el
mapper transforma resultados y la base de datos garantiza integridad.
