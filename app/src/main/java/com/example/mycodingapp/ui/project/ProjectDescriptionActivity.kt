package com.example.mycodingapp.ui.project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.mycodingapp.R
import com.example.mycodingapp.app.MyApplication
import com.example.mycodingapp.databinding.ActivityProjectDescriptionBinding
import com.example.mycodingapp.model.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectDescriptionActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityProjectDescriptionBinding
    private val binding: ActivityProjectDescriptionBinding get() = _binding
    private lateinit var project: Project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityProjectDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        project = intent.getSerializableExtra("project") as Project
        getProjectDetails()

        binding.btnGuardarCambiosProyecto.setOnClickListener {
            saveProjectChanges()
        }
    }

    private fun saveProjectChanges() {
        val updatedPriority = when {
            binding.rbBajaPrioridad.isChecked -> "Baja"
            binding.rbMediaPrioridad.isChecked -> "Media"
            binding.rbAltaPrioridad.isChecked -> "Alta"
            else -> project.prioridad

        }
        val updatedTime = binding.etTiempoEmplearProyecto.text.toString().toIntOrNull() ?: project.tiempoEmplear
        val updatedDescription = binding.etDetalleProyecto.text.toString()

        val updatedProject = project.copy(
            prioridad = updatedPriority,
            tiempoEmplear = updatedTime,
            detalle = updatedDescription
        )

        lifecycleScope.launch(Dispatchers.IO) {
            val app = applicationContext as MyApplication
            app.room.projectDao().updateProject(updatedProject)

            withContext(Dispatchers.Main) {
                Toast.makeText(this@ProjectDescriptionActivity, "Cambios guardados", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ProjectDescriptionActivity, ProjectListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getProjectDetails() {
        binding.tvNombreProyecto.text = project.nombre
        binding.tvDescripcionProyecto.text = project.descripcion
        binding.tvFechaInicioEscogida.text = project.fecha
        when (project.prioridad) {
            "Baja" -> binding.rbBajaPrioridad.isChecked = true
            "Media" -> binding.rbMediaPrioridad.isChecked = true
            "Alta" -> binding.rbAltaPrioridad.isChecked = true
        }
        binding.etTiempoEmplearProyecto.setText(project.tiempoEmplear.toString())
        binding.tvLenguajeProyectoEscogido.text = project.lenguaje
        binding.etDetalleProyecto.setText(project.detalle)
    }
}