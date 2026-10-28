package com.example.pruebaexamenrecyclerview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pruebaexamenrecyclerview.databinding.ActivityMainBinding

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

    private fun configRecycler() {
        binding.listado.layoutManager = LinearLayoutManager(this)

        adapter = ArticuloAdapter { articulo ->
            viewModel.handleArticuloSeleccionado(articulo)
        }
        binding.listado.adapter = adapter
    }

    private fun observarEstado() {
        viewModel.state.observe(this) { state ->
            state?.articuloSeleccionado.let {
                binding.textInventario.text = it
            }

            state?.articuloSeleccionado.let {
                adapter.submitList(state.articulos)
            }
        }
    }

    private fun setupEventos() {
        binding.bBorrarUltimo.setOnClickListener {
            viewModel.handleEliminarUltimo()
        }

        binding.bAnadirProducto.setOnClickListener {
            viewModel.handleAnadirArticulo()
        }
    }


}