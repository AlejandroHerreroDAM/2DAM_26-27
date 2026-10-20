package com.example.mvvm_ejercicio_alejandro.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mvvm_ejercicio_alejandro.R
import com.example.mvvm_ejercicio_alejandro.databinding.ActivityMainBinding
import com.example.mvvm_ejercicio_alejandro.di.AppModule

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            AppModule.getAllCochesUseCase,
            AppModule.addCocheUseCase,
            AppModule.removeCocheUseCase,
            AppModule.getRandomCocheUseCase
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observarEstado()
        binding.botonCocheRandom.setOnClickListener {
            viewModel.handleBotonPulsado()
        }
    }

    private fun observarEstado() {
        viewModel.state.observe(this) { state ->
            binding.ListaCoches.text = state.listaCoches.joinToString("\n") { coche ->
                "${coche.marca} ${coche.modelo} (${coche.matricula})"
            }
            binding.CocheRandom.text = state.textoPantalla
        }
    }
}
