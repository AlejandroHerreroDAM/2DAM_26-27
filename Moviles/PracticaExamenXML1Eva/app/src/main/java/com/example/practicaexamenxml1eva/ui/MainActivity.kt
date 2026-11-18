package com.example.practicaexamenxml1eva.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.practicaexamenxml1eva.databinding.ActivityMainBinding
import com.example.practicaexamenxml1eva.di.AppModule
import com.example.practicaexamenxml1eva.domain.model.Alumno


class MainActivity : AppCompatActivity() {

    // binding conecta las variables con los IDs del XML activity_main.
    private lateinit var binding: ActivityMainBinding
    // adapter muestra la lista principal de alumnos.
    private lateinit var adapter: AlumnoAdapter
    // atributoAdapter muestra los datos del alumno seleccionado.
    private lateinit var atributoAdapter: AtributoAdapter

    // viewModel conserva y modifica el estado de la pantalla.
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            AppModule.cargarAlumnosUseCase,
            AppModule.addAlumnoUseCase,
            AppModule.delAlumnoUseCase,
            AppModule.updateAlumnoUseCase
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarDesplegableCursos()
        configurarRecyclerViews()
        configurarListeners()
        observarEstado()
    }

    private fun configurarDesplegableCursos() {
        // cursos: opciones del desplegable etCurso del formulario.
        val cursos = listOf("1º DAM", "2º DAM", "1º ASIR", "2º ASIR", "1º SMR", "2º SMR")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cursos)
        binding.etCurso.setAdapter(arrayAdapter)
    }

    private fun configurarRecyclerViews() {
        // alumnoPulsado: alumno seleccionado al pulsar una fila de listado.
        adapter = AlumnoAdapter { alumnoPulsado ->
            viewModel.handleSeleccionar(alumnoPulsado)
        }
        binding.listado.adapter = adapter

        // claveAtributo: atributo pulsado dentro de rvAtributos para borrarlo.
        atributoAdapter = AtributoAdapter { claveAtributo ->
            viewModel.handleEliminarAtributo(claveAtributo)
        }
        binding.rvAtributos.adapter = atributoAdapter
    }

    private fun configurarListeners() {
        binding.buttonAddAction.setOnClickListener {
            // nuevoAlumno recoge todos los campos etNombre, etApellidos, etCurso, etEdad, etEmail y checkbox.
            val nuevoAlumno = Alumno(
                nombre = binding.etNombre.text.toString(),
                apellidos = binding.etApellidos.text.toString(),
                curso = binding.etCurso.text.toString(),
                edad = binding.etEdad.text.toString().toIntOrNull() ?: 0,
                email = binding.etEmail.text.toString(),
                activo = binding.checkboxChild1.isChecked
            )

            viewModel.handleAdd(nuevoAlumno)
            limpiarFormulario()
        }

        binding.buttonDeleteAction.setOnClickListener {
            viewModel.handleDelete()
        }
    }

    private fun observarEstado() {
        viewModel.state.observe(this) { state ->
            adapter.submitList(state.alumnos)
            binding.textTitleLista.text = "Lista de alumnos (${state.alumnos.size})"
            adapter.setAlumnoSeleccionado(state.alumnoSeleccionado?.id)

            // Mostrar u ocultar el segundo RecyclerView con los atributos del alumno seleccionado
            if (state.alumnoSeleccionado != null && state.atributosSeleccionado.isNotEmpty()) {
                binding.cardAtributos.visibility = View.VISIBLE
                atributoAdapter.submitList(state.atributosSeleccionado)
            } else {
                binding.cardAtributos.visibility = View.GONE
                atributoAdapter.submitList(emptyList())
            }

            state.error?.let { mensajeError ->
                Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show()
                viewModel.handleErrorMostrado()
            }
        }
    }

    private fun limpiarFormulario() {
        binding.etNombre.text?.clear()
        binding.etApellidos.text?.clear()
        binding.etCurso.setText("1º DAM", false)
        binding.etEdad.text?.clear()
        binding.etEmail.text?.clear()
        binding.checkboxChild1.isChecked = true
    }
}