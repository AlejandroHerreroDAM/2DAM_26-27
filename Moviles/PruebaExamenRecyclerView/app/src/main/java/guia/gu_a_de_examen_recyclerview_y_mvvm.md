# Chuleta Definitiva: RecyclerView + MVVM (Sin Use Cases)

## 🧠 1. Esquema Rápido Mental (El Orden Ideal)

Grábate este orden a fuego para no dejar código a medias en el examen:

1. **Gradle**: Activar ViewBinding.
2. **Los XML (La Carcasa)**: `activity_main.xml` (con el RecyclerView) y `item_lista.xml` (la fila).
3. **Las Data Classes (Los Datos)**: El Modelo (`Articulo`) y el Estado (`MainState`).
4. **El Adapter (Las Ruedas)**: `ListAdapter`, `ViewHolder`, `DiffUtil` y la lambda del clic.
5. **El ViewModel (El Motor)**: `MutableLiveData`, inicializar la lista en el `init`, y funciones que modifican el estado usando `.copy()`.
6. **El MainActivity (El Chasis)**: Unir ViewBinding, configurar el `LayoutManager` y el `Adapter`, observar el estado y configurar clics de botones extra.

---

## 🛠️ 2. Paso a Paso Detallado

### PASO 1: Preparación (Gradle)
Abre `build.gradle.kts (Module: app)` y añade dentro de `android { ... }`:
```kotlin
buildFeatures {
    viewBinding = true
}
```
*No olvides pulsar "Sync Now".*

### PASO 2: Los Archivos XML
*   **`activity_main.xml`**: Usa un `ConstraintLayout`. Añade un `RecyclerView` (con `0dp` para que ocupe lo que le dejes) y los botones/textos necesarios. ¡Ponles IDs claros!
*   **`item_inventario.xml`**: El diseño de una sola fila. Asegúrate de que el contenedor principal tenga `android:layout_width="match_parent"` y `android:layout_height="wrap_content"`.

### PASO 3: Los Modelos (Data Classes)
Crea tus clases puras de Kotlin:
```kotlin
data class Articulo(val nombre: String, val precio: Double)

data class MainState(
    val articulos: List<Articulo> = emptyList(),
    val textoCabecera: String = "Cargando..." // Variables extra que cambien en pantalla
)
```

### PASO 4: El Adapter (El archivo más complejo)
Pide solo el/los clics por constructor (lambdas). Hereda de `ListAdapter`.
```kotlin
class ArticuloAdapter(
    val onArticuloClick: (Articulo) -> Unit
) : ListAdapter<Articulo, ArticuloAdapter.ArticuloViewHolder>(ArticuloDiffCallback()) {

    // 1. Inflar el XML de la fila
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloViewHolder {
        val binding = ItemInventarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticuloViewHolder(binding, onArticuloClick)
    }

    // 2. Pasar los datos de la lista a la fila reciclada
    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // 3. El molde físico y pintar los datos
    class ArticuloViewHolder(
        private val binding: ItemInventarioBinding,
        private val onClick: (Articulo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(articulo: Articulo) {
            binding.tvNombreArticulo.text = articulo.nombre
            binding.root.setOnClickListener { onClick(articulo) } // Mandar el clic hacia fuera
        }
    }
}

// 4. Reglas de comparación para las animaciones automáticas
class ArticuloDiffCallback : DiffUtil.ItemCallback<Articulo>() {
    override fun areItemsTheSame(oldItem: Articulo, newItem: Articulo) = oldItem.nombre == newItem.nombre
    override fun areContentsTheSame(oldItem: Articulo, newItem: Articulo) = oldItem == newItem
}
```

### PASO 5: El ViewModel
Gestión del estado y manipulación de datos, sin tocar Android.
```kotlin
class MainViewModel : ViewModel() {
    private val _state = MutableLiveData<MainState>(MainState())
    val state: LiveData<MainState> = _state

    init {
        // Carga inicial inventada
        val cargaInicial = listOf(Articulo("Pan", 1.0), Articulo("Leche", 1.5))
        _state.value = _state.value?.copy(articulos = cargaInicial)
    }

    // Evento desde el Adapter
    fun handleFilaClickeada(articulo: Articulo) {
        _state.value = _state.value?.copy(textoCabecera = "Elegiste: ${articulo.nombre}")
    }

    // Evento desde un Botón (ej. Añadir)
    fun handleAnadir() {
        val listaActual = _state.value?.articulos ?: emptyList()
        val nuevaLista = listaActual + Articulo("Nuevo", 0.0)
        _state.value = _state.value?.copy(articulos = nuevaLista)
    }
}
```

### PASO 6: El MainActivity
La clase final que conecta los cables. Usa funciones separadas para más limpieza.
```kotlin
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels() // o ViewModelProvider(this)...
    private lateinit var adapter: ArticuloAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configRecycler()
        observarEstado()
        setupEventos()
    }

    private fun configRecycler() {
        binding.listado.layoutManager = LinearLayoutManager(this) // ¡CRÍTICO!
        adapter = ArticuloAdapter { articulo ->
            viewModel.handleFilaClickeada(articulo)
        }
        binding.listado.adapter = adapter
    }

    private fun observarEstado() {
        viewModel.state.observe(this) { state ->
            binding.textInventario.text = state.textoCabecera // Actualiza textos
            adapter.submitList(state.articulos) // Actualiza RecyclerView
        }
    }

    private fun setupEventos() {
        binding.bAnadirProducto.setOnClickListener {
            viewModel.handleAnadir()
        }
    }
}
```