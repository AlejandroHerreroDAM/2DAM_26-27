// Declara el paquete de la pantalla principal.
package com.example.mvvm_ejercicio_alejandro.ui.main

// Importa Bundle para recibir el estado guardado de la actividad.
import android.os.Bundle
// Permite dibujar la aplicación de borde a borde.
import androidx.activity.enableEdgeToEdge
// Clase base de una actividad compatible con AndroidX.
import androidx.appcompat.app.AppCompatActivity
// Utilidad para reaccionar a los márgenes del sistema.
import androidx.core.view.ViewCompat
// Proporciona los tipos de márgenes de las barras del sistema.
import androidx.core.view.WindowInsetsCompat
// Permite crear el ViewModel con una fábrica personalizada.
import androidx.lifecycle.ViewModelProvider
// Clase generada automáticamente para acceder a las vistas del layout.
import com.example.mvvm_ejercicio_alejandro.databinding.ActivityMainBinding
// Módulo que proporciona los casos de uso.
import com.example.mvvm_ejercicio_alejandro.di.AppModule

// Actividad que controla la pantalla principal de la aplicación.
class MainActivity : AppCompatActivity() {

    //AÑADIR ESTO AL GRADLE SCRIPTS, BUILD.GRADLE(MODULE:APP) DENTRO DE ANDROID
    //buildFeatures {
    //        viewBinding = true
    //    }

    // Guarda el enlace entre el layout XML y las vistas de la interfaz.
    private lateinit var binding: ActivityMainBinding
    // Guarda la referencia al ViewModel de esta pantalla.
    private lateinit var viewModel: MainViewModel

    // Método llamado por Android cuando se crea la actividad.
    override fun onCreate(savedInstanceState: Bundle?) {
        // Ejecuta primero la inicialización de la actividad padre.
        super.onCreate(savedInstanceState)
        // Permite que el contenido ocupe toda la pantalla.
        enableEdgeToEdge()

        // Infla el layout y genera el objeto de View Binding.
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Coloca la raíz del layout como contenido visible de la actividad.
        setContentView(binding.root)

        // Escucha los cambios en los márgenes de las barras del sistema.
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            // Obtiene los márgenes de la barra de estado y navegación.
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Aplica esos márgenes para evitar que el contenido quede oculto.
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            // Devuelve los márgenes para que el sistema continúe procesándolos.
            insets
        }

        // Crea una fábrica con todos los casos de uso necesarios por el ViewModel.
        val factory = MainViewModelFactory(
            // Proporciona el caso de uso que consulta todos los coches.
            AppModule.getAllCochesUseCase,
            // Proporciona el caso de uso que añade coches.
            AppModule.addCocheUseCase,
            // Proporciona el caso de uso que elimina coches.
            AppModule.removeCocheUseCase,
            // Proporciona el caso de uso que selecciona un coche aleatorio.
            AppModule.getRandomCocheUseCase,
        )
        // Obtiene o crea el ViewModel asociado a esta actividad.
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        // Empieza a observar el estado para actualizar la interfaz.
        configurarObservadores()
        // Configura las acciones de los controles de la pantalla.
        setupEventos()
    }

    // Conecta los cambios del estado del ViewModel con las vistas.
    private fun configurarObservadores(){
        // Observa el estado mientras la actividad está activa.
        viewModel.state.observe(this){
            // Recibe el nuevo estado emitido por el ViewModel.
            state -> binding.CocheRandom.text = state.textoPantalla
            // Convierte cada coche en texto y los une usando saltos de línea.
            binding.ListaCoches.text = state.listaCoches.joinToString(separator = "\n") { coche ->
                // Muestra la marca y el modelo de cada coche.
                "${coche.marca} ${coche.modelo}"
            }
        }
    }

    // Configura los listeners de los eventos de la interfaz.
    private fun setupEventos() {
        // Reacciona cuando el usuario pulsa el botón.
        binding.botonCocheRandom.setOnClickListener {
            // Pide al ViewModel que seleccione y muestre un coche aleatorio.
            viewModel.handleBotonPulsado()
        }
    }

}