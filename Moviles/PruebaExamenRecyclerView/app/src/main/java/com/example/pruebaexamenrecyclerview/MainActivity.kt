package com.example.pruebaexamenrecyclerview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pruebaexamenrecyclerview.databinding.ActivityMainBinding

// VISTA: Une el diseño (XML) con la lógica (ViewModel).
// No toma decisiones, solo obedece a lo que diga el MainState y envía los clics al ViewModel.
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: ArticuloAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        configRecycler()
        observarEstado()
        setupEventos()
    }

    // CONFIGURAR RECYCLER: Asigna el modo lista y el adapter.
    private fun configRecycler() {
        binding.listado.layoutManager = LinearLayoutManager(this) // IMPORTANTE: Sin esto no se ve la lista

        adapter = ArticuloAdapter { articulo ->
            // Cuando el Adapter avisa de un clic en la fila, se lo pasamos al ViewModel
            viewModel.handleArticuloSeleccionado(articulo)
        }
        binding.listado.adapter = adapter
    }

    // ESCUCHAR AL CEREBRO: Cada vez que el ViewModel cambia el estado, actualizamos la pantalla.
    private fun observarEstado() {
        viewModel.state.observe(this) { state ->
            // Actualiza el texto superior
            state?.articuloSeleccionado.let {
                binding.textInventario.text = it
            }
            // Le pasa la lista actual (sea la inicial, o tras añadir/borrar) al Adapter para que dibuje
            state?.articuloSeleccionado.let {
                adapter.submitList(state.articulos)
            }
        }
    }

    // BOTONES: Asigna a los botones del activity_main.xml a qué función del ViewModel deben llamar.
    private fun setupEventos() {
        binding.bBorrarUltimo.setOnClickListener {
            viewModel.handleEliminarUltimo()
        }

        binding.bAnadirProducto.setOnClickListener {
            viewModel.handleAnadirArticulo()
        }
    }
}