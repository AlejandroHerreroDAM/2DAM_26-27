package com.example.mvvm_ejercicio_alejandro.ui.main
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm_ejercicio_alejandro.databinding.ActivityMainBinding
import com.example.mvvm_ejercicio_alejandro.di.AppModule
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val factory = MainViewModelFactory(
            AppModule.getAllCochesUseCase,
            AppModule.addCocheUseCase,
            AppModule.removeCocheUseCase,
            AppModule.getRandomCocheUseCase,
        )
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        configurarObservadores()
        setupEventos()
    }
    private fun configurarObservadores(){
        viewModel.state.observe(this){
            state -> binding.CocheRandom.text = state.textoPantalla
            binding.ListaCoches.text = state.listaCoches.joinToString(separator = "\n") { coche ->
                "${coche.marca} ${coche.modelo}"
            }
        }
    }
    private fun setupEventos() {
        binding.botonCocheRandom.setOnClickListener {
            viewModel.handleBotonPulsado()
        }
    }

}