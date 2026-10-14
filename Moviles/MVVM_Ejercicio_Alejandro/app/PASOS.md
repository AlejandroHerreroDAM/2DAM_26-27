# Guía Completa: Arquitectura Limpia, MVVM y UDF en Android

Esta es la línea de tiempo detallada paso a paso que has seguido para construir la aplicación de Coches, separando las responsabilidades en capas.

## PASO 1: La Base (Capa Domain - Modelos)
El primer paso siempre es definir *qué* vas a manejar en tu aplicación, sin depender de ninguna librería de Android.
1. Crea el paquete `domain.model`.
2. Crea la `data class` principal (ej: `Coche`) con sus atributos (`marca`, `modelo`, etc.). Todo inmutable (`val`).

## PASO 2: El Almacén (Capa Data)
Aquí decides de dónde salen los datos. En este caso, simulamos una base de datos local en memoria.
1. Crea el paquete `data`.
2. Crea un patrón Singleton usando un `object` (ej: `DBCoches`).
3. Define una lista privada y mutable (`_listaCoches`) con datos de prueba.
4. Crea las funciones necesarias para manipular o consultar esa lista:
    * `getAllCoches()`: devuelve una lista inmutable (copia).
    * `addCoche(c: Coche)`
    * `removeCoche(c: Coche)`

## PASO 3: Las Acciones Permitidas (Capa Domain - Use Cases)
La vista no debe hablar directamente con la base de datos. Los Casos de Uso son los intermediarios que representan las acciones que un usuario puede hacer.
1. Crea el paquete `domain.useCases`.
2. Crea una clase para cada acción específica:
    * `GetAllCochesUseCase`: llama a `DBCoches.getAllCoches()`.
    * `AddCocheUseCase`: llama a `DBCoches.addCoche(c)`.
    * `RemoveCocheUseCase`: llama a `DBCoches.removeCoche(c)`.

## PASO 4: El Surtidor de Herramientas (Capa DI)
Para no instanciar los Casos de Uso constantemente, se preparan en un único lugar (Inyección de Dependencias manual).
1. Crea el paquete `di`.
2. Crea un `object AppModule`.
3. Instancia dentro como constantes estáticas todos tus Casos de Uso:
    * `val getAllCochesUseCase = GetAllCochesUseCase()`
    * `val addCocheUseCase = AddCocheUseCase()`

## PASO 5: La "Fotografía" Visual (Capa UI - El Estado)
Iniciamos la Capa UI definiendo qué datos necesita la pantalla para pintarse.
1. Crea el paquete `ui.main`.
2. Crea la `data class MainState`.
3. Añade los atributos inmutables (`val`) con valores por defecto. Si no hay RecyclerView y mostramos texto:
    * `val listaCoches: List<Coche> = emptyList()`
    * `val error: String? = null`

## PASO 6: El Cerebro (Capa UI - ViewModel y Factory)
El ViewModel orquesta la lógica y actualiza el Estado (Flujo Unidireccional de Datos).
1. Crea la clase `MainViewModel` heredando de `ViewModel`.
2. Pásale por constructor los Casos de Uso.
3. Define el Estado interno (mutable) y externo (inmutable):
    * `private val _state = MutableLiveData<MainState>(MainState())`
    * `val state: LiveData<MainState> = _state`
4. Añade un bloque `init { handleGetAllCoches() }` si necesitas cargar datos al abrir la app.
5. Crea las funciones `handle...` que reaccionan a los botones de la vista. Dentro de ellas, llama a los casos de uso y actualiza el estado siempre usando `.copy()`:
    * `_state.value = _state.value?.copy(listaCoches = ...)`
6. Abajo en el mismo archivo, crea el `MainViewModelFactory` para poder decirle a Android cómo debe construir el ViewModel pasándole los Casos de Uso del `AppModule`.

## PASO 7: Configuración Previa de la Vista (Gradle)
Antes de programar la UI, prepara el terreno.
1. Abre el archivo `build.gradle.kts (Module: app)`.
2. Dentro del bloque `android { ... }`, activa el ViewBinding:
   ```kotlin
   buildFeatures {
       viewBinding = true
   }
   ```
3. Pulsa "Sync Now".

## PASO 8: El Diseño Visual (XML)
1. Abre `activity_main.xml`.
2. Usa `ConstraintLayout` para posicionar los elementos.
3. Define identificadores claros (`android:id="@+id/..."`) para tus TextViews y Botones.

## PASO 9: La Conexión Final (MainActivity)
La Activity es "tonta": solo pinta lo que dice el Estado y avisa al ViewModel de los clicks.
1. Declara `binding` (`ActivityMainBinding`) y `viewModel` (`MainViewModel`).
2. En `onCreate`, inicializa el `binding` e infla la vista.
3. Instancia el `Factory` inyectándole las variables del `AppModule`.
4. Inicializa el `viewModel` usando `ViewModelProvider(this, factory)`.
5. Configura los observadores (`viewModel.state.observe...`). Aquí conectas el estado con el binding (ej: transformando la lista en un String con `.joinToString("\n")`).
6. Configura los eventos (`binding.boton.setOnClickListener...`) para que llamen a las funciones `handle` del ViewModel.