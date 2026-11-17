# 📚 Guía Definitiva de Concurrencia y Programación Asíncrona en Java 25 (2º DAM - PSP)

Esta guía reúne y estructura todos los conceptos, patrones, APIs modernas de Java 25 y buenas prácticas vistas en clase, desde la creación básica de hilos hasta la composición asíncrona con `CompletableFuture` y `@Async`.

## 📋 Índice de Contenidos

 1. [Fundamentos de Hilos: `Thread`, `Runnable` e Hilos Virtuales](#1-fundamentos-de-hilos-thread-runnable-e-hilos-virtuales)

 2. [Ciclo de Vida y Control de Hilos](#2-ciclo-de-vida-y-control-de-hilos)

 3. [Condiciones de Carrera, `synchronized` y Deadlocks](#3-condiciones-de-carrera-synchronized-y-deadlocks)

 4. [Comunicación Clásica entre Hilos: `wait()` y `notifyAll()`](#4-comunicación-clásica-entre-hilos-wait-y-notifyall)

 5. [Herramientas de Alto Nivel: Locks, Semáforos y Atómicos](#5-herramientas-de-alto-nivel-locks-semáforos-y-atómicos)

 6. [Colecciones Seguras para Hilos (*Thread-Safe*)](#6-colecciones-seguras-para-hilos-thread-safe)

 7. [Pools de Hilos: `ExecutorService`, `Callable` y `Future`](#7-pools-de-hilos-executorservice-callable-y-future)

 8. [Sincronizadores de Fases: `CountDownLatch` y `CyclicBarrier`](#8-sincronizadores-de-fases-countdownlatch-y-cyclicbarrier)

 9. [Programación Asíncrona con `CompletableFuture`](#9-programación-asíncrona-con-completablefuture)

10. [Introducción a la Asincronía en Spring Boot (`@Async`)](#10-introducción-a-la-asincronía-en-spring-boot-async)

11. [Chuleta de Examen: ¿Qué herramienta uso y qué errores evitar?](#11-chuleta-de-examen-qué-herramienta-uso-y-qué-errores-evitar)

## 1. Fundamentos de Hilos: `Thread`, `Runnable` e Hilos Virtuales

### Proceso vs. Hilo

* **Proceso:** Programa en ejecución con su propio espacio de memoria aislado asignado por el Sistema Operativo.

* **Hilo (*Thread*):** Unidad básica de ejecución dentro de un proceso. Todos los hilos de un mismo proceso **comparten la misma memoria (Heap)**, lo que permite comunicarse rápido, pero obliga a sincronizar el acceso a los datos compartidos.

### Formas de crear un hilo

#### A) Extendiendo `Thread` vs. Implementando `Runnable`

Siempre se prefiere **`Runnable`** (o expresiones lambda) porque Java no tiene herencia múltiple: implementar `Runnable` permite que tu clase herede de otra y separa *qué hace la tarea* de *cómo se ejecuta*.

```
// 1. Con Runnable y sintaxis clásica
Runnable tarea = () -> {
    System.out.println("Ejecutando en: " + Thread.currentThread().getName());
};
Thread hiloClasico = new Thread(tarea, "Hilo-Trabajador");
hiloClasico.start();

// 2. API Moderna (Java 21/25) para Hilos de Plataforma (SO)
Thread hiloPlataforma = Thread.ofPlatform()
        .name("Plataforma-1")
        .start(tarea);

```

> ⚠️ **El error clásico de examen (`run()` vs `start()`):**
> Llamar a `hilo.run()` **NO crea un hilo nuevo**; ejecuta el método de forma secuencial dentro del propio hilo `main`. Siempre debes llamar a **`hilo.start()`** para que la JVM cree una nueva pila de ejecución y planifique el hilo.

#### B) Hilos Virtuales (`Thread.ofVirtual()`) — Java 21 / 25

Los hilos de plataforma tienen una correspondencia $1:1$ con los hilos del Sistema Operativo (son pesados, ocupan $\approx 1\text{ MB}$ de RAM y crear miles satura el sistema).
Los **hilos virtuales** son gestionados por la propia JVM sobre un pequeño pool de hilos del SO (*carrier threads*). Son ultraligeros: puedes lanzar 100.000 sin problemas, ideales para tareas con esperas de E/S (*I/O*, red, `sleep`, bases de datos).

```
List<Thread> hilosVirtuales = new ArrayList<>();

for (int i = 0; i < 10_000; i++) {
    int id = i;
    Thread vThread = Thread.ofVirtual()
            .name("Virtual-" + id)
            .start(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
    hilosVirtuales.add(vThread);
}

// ¡MUY IMPORTANTE! Los hilos virtuales son DAEMON por defecto.
// Si el main no hace join(), el programa termina inmediatamente sin esperarlos.
for (Thread t : hilosVirtuales) {
    t.join();
}

```

#### C) Hilos *Daemon* vs. Hilos de Usuario

* **Hilo de usuario:** La JVM espera a que todos los hilos de usuario terminen antes de cerrarse.

* **Hilo *daemon* (`t.setDaemon(true)`):** Hilo de servicio en segundo plano (como el recolector de basura). Cuando solo quedan hilos *daemon* vivos, **la JVM se apaga de golpe** sin esperarlos.

## 2. Ciclo de Vida y Control de Hilos

### Estados de un hilo (`Thread.getState()`)

1. `NEW`: Instanciado (`new Thread(...)`), aún no se ha llamado a `start()`.

2. `RUNNABLE`: Listo para ejecutarse o ejecutándose actualmente en la CPU.

3. `BLOCKED`: Esperando a adquirir un monitor (`synchronized`) ocupado por otro hilo.

4. `WAITING`: Esperando indefinidamente a que otro hilo lo despierte (`wait()`, `join()`).

5. `TIMED_WAITING`: Esperando durante un tiempo máximo (`Thread.sleep(ms)`, `wait(ms)`, `join(ms)`).

6. `TERMINATED`: El método `run()` ha finalizado (o ha lanzado una excepción no capturada).

### Métodos clave de control

* **`Thread.sleep(ms)`:** Pausa el hilo actual durante los milisegundos indicados. No libera ningún lock que posea.

* **`hilo.join()`:** Hace que el hilo que llama (por ejemplo, `main`) espere hasta que `hilo` pase al estado `TERMINATED`.

* **`hilo.interrupt()`:** En Java, la interrupción es **cooperativa** (no mata el hilo a la fuerza). Activa una bandera interna (`isInterrupted() == true`) y, si el hilo estaba bloqueado en `sleep()`, `wait()` o `join()`, lanza `InterruptedException`.

```
Thread cocinero = Thread.ofPlatform().start(() -> {
    try {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Cocinando a fuego lento...");
            Thread.sleep(1000);
        }
    } catch (InterruptedException e) {
        // Al saltar InterruptedException, Java limpia la bandera de interrupción.
        // Es buena práctica restaurarla si vas a propagar el estado:
        Thread.currentThread().interrupt();
        System.out.println("Cocción cancelada limpiamente.");
    }
});

Thread.sleep(2500);
cocinero.interrupt(); // Solicita la parada
cocinero.join();      // Espera a que termine de recoger

```

## 3. Condiciones de Carrera, `synchronized` y Deadlocks

### Condición de Carrera (*Race Condition*) y `volatile`

Ocurre cuando dos o más hilos acceden a un recurso compartido mutable al mismo tiempo y el resultado depende del orden en que el planificador los ejecute.
Una operación como `contador++` **no es atómica**; consta de 3 pasos: leer valor, sumar 1, escribir valor. Si dos hilos leen el mismo valor a la vez, se pierde un incremento.

> ❌ **¿Sirve `volatile` para evitar condiciones de carrera?**
> **No.** `volatile` garantiza **visibilidad** (que todos los hilos lean el valor actualizado de la memoria principal y no de la caché del núcleo), pero **no garantiza atomicidad** para operaciones compuestas como `saldo -= cantidad` o `contador++`.

### Exclusión mutua con `synchronized`

Garantiza que solo un hilo pueda ejecutar a la vez una sección crítica protegida por el **monitor intrínseco** de un objeto.

```
public class CuentaBancaria {
    private double saldo = 1000.0;
    private final Object lockPrivado = new Object();

    // Opción 1: Sincronizar el método completo (usa 'this' como monitor)
    public synchronized void ingresar(double cantidad) {
        saldo += cantidad;
    }

    // Opción 2: Bloque sincronizado (permite reducir la sección crítica y usar un lock privado)
    public boolean retirar(double cantidad) {
        // Aquí podría ir código de validación que no necesita bloqueo...
        synchronized (lockPrivado) {
            if (saldo >= cantidad) {
                saldo -= cantidad;
                return true;
            }
            return false;
        }
    }
}

```

### Deadlock (Interbloqueo)

Ocurre cuando dos o más hilos se bloquean mutuamente para siempre esperando recursos que tiene el otro:

* Hilo 1 bloquea `A` y espera `B`.

* Hilo 2 bloquea `B` y espera `A`.

**Cómo evitarlo en el examen:**

1. **Orden global de adquisición:** Todos los hilos deben adquirir los locks exactamente en el **mismo orden** (por ejemplo, ordenando cuentas bancarias por su `id` numérico).

2. **Usar `tryLock(tiempo, unidad)`** con `ReentrantLock` para soltar el primer lock si no se consigue el segundo.

```
public void transferir(Cuenta origen, Cuenta destino, double importe) {
    // Ordenamos los locks por ID para evitar el deadlock circular
    Cuenta primero = origen.getId() < destino.getId() ? origen : destino;
    Cuenta segundo = origen.getId() < destino.getId() ? destino : origen;

    synchronized (primero) {
        synchronized (segundo) {
            if (origen.retirar(importe)) {
                destino.ingresar(importe);
            }
        }
    }
}

```

## 4. Comunicación Clásica entre Hilos: `wait()` y `notifyAll()`

Permite coordinar hilos (patrón **Productor-Consumidor**) cuando un hilo debe esperar a que se cumpla una condición sobre un objeto compartido.

### 3 Reglas de Oro de `wait()` / `notifyAll()`

1. **Siempre dentro de `synchronized`:** Llamar a `wait()` o `notifyAll()` sin poseer el monitor del objeto lanza `IllegalMonitorStateException`. (Al llamar a `wait()`, el hilo **libera el lock** y se duerme hasta que alguien llame a `notifyAll()`).

2. **Comprobar la condición con `while`, NUNCA con `if`:** Protege contra despertares espurios y evita que, al despertar varios hilos, uno se cuele cuando otro ya volvió a llenar/vaciar el buffer.

3. **Usar `notifyAll()` en lugar de `notify()`:** Si hay varios productores y consumidores esperando en el mismo monitor, `notify()` podría despertar al hilo equivocado y provocar un bloqueo.

```
public class BufferManual<T> {
    private final Queue<T> cola = new LinkedList<>();
    private final int capacidad;

    public BufferManual(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void producir(T item) throws InterruptedException {
        while (cola.size() == capacidad) {
            wait(); // Libera el monitor y espera a que haya hueco
        }
        cola.add(item);
        notifyAll(); // Avisa a los consumidores
    }

    public synchronized T consumir() throws InterruptedException {
        while (cola.isEmpty()) {
            wait(); // Libera el monitor y espera a que haya elementos
        }
        T item = cola.poll();
        notifyAll(); // Avisa a los productores
        return item;
    }
}

```

## 5. Herramientas de Alto Nivel: Locks, Semáforos y Atómicos

El paquete `java.util.concurrent` ofrece alternativas más flexibles y eficientes que `synchronized`.

### A) `ReentrantLock`

Ofrece las mismas garantías de exclusión mutua que `synchronized`, pero permite intentar adquirir el lock con tiempo límite (`tryLock`), interrumpir la espera o activar equidad (*fairness* FIFO con `new ReentrantLock(true)`).

> ⚠️ **Obligatorio:** Siempre libera el lock en un bloque **`finally`**. Si ocurre una excepción dentro del `try` y no hay `finally`, el lock queda bloqueado para siempre.

```
ReentrantLock lock = new ReentrantLock();

public void operacionSegura() {
    try {
        if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {
            try {
                // Sección crítica
            } finally {
                lock.unlock(); // SIEMPRE en finally
            }
        } else {
            System.out.println("Recurso ocupado tras 500ms, reintentando...");
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}

```

### B) `Semaphore` (Control de $N$ recursos limitados)

Mientras un lock permite pasar a **1 hilo**, un semáforo gestiona $N$ **permisos** simultáneos (ej. 3 plazas de parking, 2 hornos, 20 conexiones a BD).

* `acquire()`: Toma 1 permiso (o bloquea el hilo si `availablePermits() == 0`).

* `release()`: Devuelve 1 permiso (siempre en `finally`).

```
Semaphore hornos = new Semaphore(2, true); // 2 hornos, modo justo (FIFO)

public void hornearPizza(int idPedido) throws InterruptedException {
    hornos.acquire();
    try {
        System.out.println("Horneando pedido " + idPedido + ". Hornos libres: " + hornos.availablePermits());
        Thread.sleep(1000);
    } finally {
        hornos.release();
    }
}

```

### C) Variables Atómicas (`AtomicInteger`, `AtomicLong`, `DoubleAdder`)

Utilizan instrucciones de hardware CAS (*Compare-And-Swap*) sin necesidad de bloquear hilos. Son muchísimo más rápidas que `synchronized` para contadores o acumuladores simples.

```
AtomicInteger visitas = new AtomicInteger(0);
visitas.incrementAndGet(); // Equivalente atómico a ++visitas
visitas.addAndGet(5);      // Suma 5 de forma atómica

// Para sumas de decimales con altísima concurrencia:
DoubleAdder ingresosTotales = new DoubleAdder();
ingresosTotales.add(45.50);
double total = ingresosTotales.sum();

```

## 6. Colecciones Seguras para Hilos (*Thread-Safe*)

Nunca uses `ArrayList` o `HashMap` normales si varios hilos escriben en ellos a la vez (lanzarán `ConcurrentModificationException` o corromperán los datos).

### A) `BlockingQueue` (Patrón Productor-Consumidor sin `wait/notify`)

Sustituye por completo el código manual del Bloque 4. Implementaciones principales: `ArrayBlockingQueue<>(capacidad)` (acotada) y `LinkedBlockingQueue<>()`.

* `put(e)`: Inserta el elemento; **bloquea** si la cola está llena.

* `take()`: Extrae el primer elemento; **bloquea** si la cola está vacía.

* `poll(tiempo, unidad)`: Intenta extraer esperando como máximo el tiempo indicado; devuelve `null` si vence el tiempo (muy útil para que los consumidores no se queden colgados al final del programa).

```
BlockingQueue<String> mesaPedidos = new ArrayBlockingQueue<>(10);

// Productor (Camarero)
mesaPedidos.put("Pizza Margarita");

// Consumidor (Cocinero)
String pedido = mesaPedidos.poll(500, TimeUnit.MILLISECONDS);
if (pedido != null) {
    // Preparar pedido
}

```

### B) `ConcurrentHashMap`

Mapa concurrente de alto rendimiento que bloquea solo segmentos internos en lugar de toda la tabla.

* **`merge(clave, valor, remappingFunction)`:** Actualiza contadores por clave de forma atómica en una sola línea.

```
ConcurrentHashMap<String, Integer> platosVendidos = new ConcurrentHashMap<>();

// Si "Paella" no existe, pone 1. Si existe, suma 1 al valor actual de forma atómica:
platosVendidos.merge("Paella", 1, Integer::sum);

```

### C) `Collections.synchronizedList(new ArrayList<>())`

Envuelve una lista normal para hacer atómicas las operaciones individuales (`add`, `get`). **Importante:** si vas a recorrerla con un bucle `for-each`, debes envolver el bucle en un bloque `synchronized(lista)`.

## 7. Pools de Hilos: `ExecutorService`, `Callable` y `Future`

Crear hilos manualmente para cada tarea tiene coste. Un `ExecutorService` gestiona un conjunto (*pool*) de hilos reutilizables.

### Tipos principales de Pools (`Executors`)

* `Executors.newFixedThreadPool(n)`: Pool fijo de $n$ hilos. Ideal para limitar carga de CPU o conexiones.

* `Executors.newCachedThreadPool()`: Crea hilos bajo demanda y los reutiliza si quedan libres. Ideal para muchas tareas cortas en ráfaga.

* `Executors.newSingleThreadExecutor()`: 1 único hilo; garantiza ejecución secuencial en orden FIFO.

* `Executors.newVirtualThreadPerTaskExecutor()`: (Java 21+) Crea un hilo virtual nuevo para cada tarea enviada.

### `Callable<T>` y `Future<T>`

* `Runnable` ejecuta una acción y devuelve `void`.

* `Callable<T>` ejecuta una acción, **devuelve un resultado de tipo `T`** y puede lanzar excepciones comprobadas.

* `Future<T>` es el "recibo" que devuelve `executor.submit(callable)` para recoger el resultado en el futuro.

```
// En Java 19+, ExecutorService implementa AutoCloseable (se puede usar en try-with-resources)
try (ExecutorService executor = Executors.newFixedThreadPool(3)) {

    Callable<Integer> tarea1 = () -> { Thread.sleep(1000); return 10; };
    Callable<Integer> tarea2 = () -> { Thread.sleep(2000); return 25; };

    // Opción A: Envío individual con submit()
    Future<Integer> f1 = executor.submit(tarea1);
    Future<Integer> f2 = executor.submit(tarea2);

    // f.get() BLOQUEA el hilo actual hasta que el resultado esté listo
    int suma = f1.get() + f2.get();
    System.out.println("Suma: " + suma);

    // Opción B: Lanzar una lista de Callables a la vez y esperar a todas con invokeAll()
    List<Future<Integer>> todos = executor.invokeAll(List.of(tarea1, tarea2));
    for (Future<Integer> f : todos) {
        System.out.println("Resultado invokeAll: " + f.get());
    }
} // Al salir del bloque try, llama automáticamente a shutdown() y espera a que terminen

```

> **Cierre clásico (sin `try-with-resources`):**
>
> ```
> executor.shutdown(); // No acepta tareas nuevas, termina las pendientes
> if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
>     executor.shutdownNow(); // Fuerza interrupción si tarda demasiado
> }
> 
> ```

## 8. Sincronizadores de Fases: `CountDownLatch` y `CyclicBarrier`

### A) `CountDownLatch` (Cuenta atrás de eventos)

Permite que uno o varios hilos esperen (`await()`) hasta que una serie de operaciones en otros hilos finalicen (`countDown()`). **No es reutilizable** (una vez llega a 0, queda abierto para siempre).

```
int numPedidos = 15;
CountDownLatch latch = new CountDownLatch(numPedidos);

// Cada trabajador hace esto al acabar un pedido:
latch.countDown(); // Resta 1 al contador

// El hilo main espera aquí hasta que el contador llegue a 0:
latch.await();
System.out.println("Todos los pedidos terminados. Cerrando sistema.");

```

### B) `CyclicBarrier` (Punto de encuentro entre hilos)

Permite que un grupo fijo de $N$ hilos se esperen unos a otros en una barrera (`await()`) antes de continuar todos a la vez. **Es reutilizable** (ideal para procesos por fases) y admite una acción opcional que ejecuta el último hilo en llegar antes de abrir la barrera.

```
CyclicBarrier barreraApertura = new CyclicBarrier(3, () -> {
    System.out.println(">> ¡Los 3 cocineros están listos! Abre el restaurante.");
});

// Dentro del código de cada uno de los 3 cocineros:
Thread.sleep(300); // Preparan su estación
barreraApertura.await(); // Se quedan aquí hasta que lleguen los 3
// A partir de aquí empiezan a cocinar a la vez

```

| Característica | `CountDownLatch` | `CyclicBarrier` | 
| ----- | ----- | ----- | 
| **¿Qué espera?** | A que ocurran $N$ eventos (`countDown`). | A que lleguen $N$ hilos al punto `await()`. | 
| **¿Quién desbloquea?** | Cualquier hilo llamando a `countDown()`. | Los propios $N$ hilos participantes al llegar. | 
| **¿Reutilizable?** | ❌ No (un solo uso). | ✅ Sí (se reinicia tras abrirse). | 

## 9. Programación Asíncrona con `CompletableFuture`

El problema de `Future.get()` es que bloquea el hilo. `CompletableFuture` permite construir **tuberías (*pipelines*) reactivas** donde encadenas qué hacer cuando un resultado esté listo, sin bloquear hilos innecesariamente.

> 💡 **Concepto clave de examen:**
>
> * **Asincronía:** Lanzar una tarea sin quedarse bloqueado esperando su respuesta inmediata.
>
> * **Concurrencia:** Varias tareas progresando en el mismo intervalo de tiempo.
>
> * **Paralelismo:** Varias tareas ejecutándose físicamente a la vez en distintos núcleos de CPU.

### 1. Lanzar tareas (`runAsync` vs `supplyAsync`)

Si no pasas un `Executor`, usa `ForkJoinPool.commonPool()`. En aplicaciones reales o exámenes, **pasa siempre tu propio `ExecutorService`**.

```
ExecutorService pool = Executors.newFixedThreadPool(4);

// Sin valor de retorno -> CompletableFuture<Void>
CompletableFuture<Void> cfVacio = CompletableFuture.runAsync(() -> {
    System.out.println("Guardando log en segundo plano...");
}, pool);

// Con valor de retorno -> CompletableFuture<Integer>
CompletableFuture<Integer> cfNumero = CompletableFuture.supplyAsync(() -> {
    return 21;
}, pool);

```

### 2. Encadenar operaciones (`thenApply`, `thenAccept`, `thenRun`)

* **`thenApply(fn)`:** Transforma el resultado y devuelve un nuevo valor (como `.map()` en Streams).

* **`thenAccept(consumer)`:** Consume el resultado final y devuelve `Void` (ej. imprimir o guardar).

* **`thenRun(runnable)`:** Ejecuta una acción al terminar, ignorando el resultado anterior.

```
CompletableFuture.supplyAsync(() -> 10, pool)
        .thenApply(n -> n * 2)          // 20
        .thenApply(n -> "Total: " + n)  // "Total: 20"
        .thenAccept(System.out::println) // Imprime "Total: 20"
        .join(); // Espera al final de toda la cadena (no lanza checked exceptions como get())

```

### 3. `thenApply()` vs `thenCompose()` (¡Pregunta típica de examen!)

* Usa **`thenApply`** cuando la función siguiente devuelve un **valor normal** (`T -> U`).

* Usa **`thenCompose`** cuando la función siguiente **ya devuelve otro `CompletableFuture`** (`T -> CompletableFuture<U>`), evitando tener un `CompletableFuture<CompletableFuture<U>>` anidado (como `.flatMap()`).

```
CompletableFuture<String> obtenerUsuarioAsync(int id) { /* ... */ }
CompletableFuture<String> obtenerEmailAsync(String usuario) { /* ... */ }

// Encadenamiento asíncrono limpio sin bloquear:
CompletableFuture<String> flujoEmail = obtenerUsuarioAsync(1)
        .thenCompose(usuario -> obtenerEmailAsync(usuario));

```

### 4. Combinar tareas paralelas (`thenCombine`, `allOf`, `anyOf`)

```
CompletableFuture<Double> cfPrecio = CompletableFuture.supplyAsync(() -> 50.0, pool);
CompletableFuture<Double> cfEnvio  = CompletableFuture.supplyAsync(() -> 4.99, pool);

// A) thenCombine: Une los resultados de DOS tareas independientes en paralelo
CompletableFuture<Double> cfTotal = cfPrecio.thenCombine(cfEnvio, (precio, envio) -> precio + envio);

// B) allOf: Espera a que terminen N tareas independientes (devuelve CompletableFuture<Void>)
CompletableFuture<Void> todas = CompletableFuture.allOf(cfPrecio, cfEnvio);
todas.join(); // Cuando pasa de aquí, ambas han terminado y sus .join() son instantáneos

// C) anyOf: Termina en cuanto acabe la PRIMERA de las tareas
CompletableFuture<Object> primera = CompletableFuture.anyOf(cfPrecio, cfEnvio);

```

### 5. Manejo de Errores y Timeouts

Las tareas asíncronas capturan las excepciones internamente. Si no las gestionas, saltarán al llamar a `join()` o `get()`.

```
CompletableFuture<String> consultaApi = CompletableFuture.supplyAsync(() -> {
            // Simula operación lenta o que puede fallar
            if (Math.random() < 0.3) throw new RuntimeException("Fallo de red");
            return "Datos OK";
        }, pool)
        // Si tarda más de 2 segundos, falla con TimeoutException:
        .orTimeout(2, TimeUnit.SECONDS)
        // Recupera cualquier error previo (incluyendo el TimeoutException) con un valor por defecto:
        .exceptionally(error -> {
            System.err.println("Aviso: " + error.getMessage());
            return "DATOS EN CACHÉ (FALLBACK)";
        });

// Alternativa a exceptionally cuando quieres inspeccionar resultado Y error a la vez:
CompletableFuture<Integer> divisionSegura = CompletableFuture.supplyAsync(() -> 10 / 0)
        .handle((resultado, error) -> (error != null) ? 0 : resultado);

// Alternativa directa para timeout con valor por defecto sin lanzar excepción:
CompletableFuture<String> conFallbackTimeout = CompletableFuture.supplyAsync(() -> "OK")
        .completeOnTimeout("POR DEFECTO", 2, TimeUnit.SECONDS);

```

## 10. Introducción a la Asincronía en Spring Boot (`@Async`)

En aplicaciones Spring Boot, el framework abstrae la gestión de pools mediante anotaciones:

1. **Activar asincronía** en la clase principal o de configuración con `@EnableAsync`.

2. **Definir un pool personalizado** (recomendado para no usar el executor simple por defecto):

```
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "miPool")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("SpringAsync-");
        executor.initialize();
        return executor;
    }
}

```

3. **Marcar el método del servicio** devolviendo `CompletableFuture<T>` si necesitamos recoger el resultado:

```
@Service
public class InformeService {
    @Async("miPool")
    public CompletableFuture<String> generarInformeAsync() {
        // Se ejecuta en un hilo "SpringAsync-X"
        return CompletableFuture.completedFuture("Informe listo");
    }
}

```

> ⚠️ **La trampa del proxy en `@Async`:**
> Spring implementa `@Async` envolviendo el bean en un *Proxy*. Si un método de `MiServicio` llama a otro método `@Async` **dentro de la misma clase** (`this.metodoAsync()`), la llamada **no pasa por el proxy** y se ejecutará de forma **síncrona** en el mismo hilo. Siempre debe llamarse desde otro bean distinto (por ejemplo, desde un `Controller` hacia un `Service`).

## 11. Chuleta de Examen: ¿Qué herramienta uso y qué errores evitar?

### Tabla de Decisión Rápida

| Escenario en el enunciado | Herramienta recomendada | 
| ----- | ----- | 
| Lanzar miles de tareas ligeras con esperas (clientes, peticiones) | `Thread.ofVirtual()` | 
| Contador o acumulador compartido entre varios hilos | `AtomicInteger`, `AtomicLong`, `DoubleAdder` | 
| Proteger una sección crítica o invariante de varias variables | `synchronized` o `ReentrantLock` | 
| Limitar acceso a $N$ recursos iguales (plazas, hornos, pistas) | `Semaphore(N, true)` | 
| Productor-Consumidor con buffer limitado (camareros/cocineros) | `ArrayBlockingQueue<>(capacidad)` | 
| Estadísticas o caché compartida clave-valor | `ConcurrentHashMap` + `.merge()` | 
| Pool fijo de trabajadores que devuelven resultados | `ExecutorService` + `Callable<T>` + `Future<T>` | 
| Esperar a que terminen $N$ tareas antes de cerrar el programa | `CountDownLatch(N)` | 
| Hacer que $N$ hilos empiecen todos a la vez tras prepararse | `CyclicBarrier(N)` | 
| Ejecutar 2 APIs en paralelo y juntar sus resultados sin bloquear | `CompletableFuture.supplyAsync()` + `thenCombine()` | 
| Evitar que una llamada externa deje el programa colgado | `.orTimeout()` + `.exceptionally()` | 

### Checklist de Errores Fatales en Examen

1. **Olvidar `join()` en hilos virtuales:** Al ser *daemon*, el `main` acaba y mata todos los hilos antes de que hagan nada.

2. **Liberar `Lock` o `Semaphore` fuera de `finally`:** Si salta una excepción, el recurso queda bloqueado para siempre (*resource leak*).

3. **Bloquear con `.join()` o `.get()` demasiado pronto:** Si haces `cf1.join()` antes de lanzar `cf2`, estás ejecutando las tareas de forma secuencial en lugar de paralela. Lanza primero todos los `CompletableFuture` y luego combínalos (`thenCombine` / `allOf`).

4. **No cerrar el `ExecutorService`:** Si no llamas a `executor.shutdown()` (o usas `try-with-resources`), los hilos del pool siguen vivos y **la aplicación Java nunca termina**.

5. **Acquirir múltiples locks en distinto orden:** Provoca un **Deadlock** inmediato bajo carga. Establece siempre un orden fijo.