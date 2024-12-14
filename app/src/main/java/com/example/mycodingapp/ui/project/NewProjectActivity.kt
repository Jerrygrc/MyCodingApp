package com.example.mycodingapp.ui.project

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.mycodingapp.R
import com.example.mycodingapp.app.MyApplication
import com.example.mycodingapp.databinding.ActivityNewProjectBinding
import com.example.mycodingapp.model.Language
import com.example.mycodingapp.model.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewProjectActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityNewProjectBinding
    private val binding: ActivityNewProjectBinding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityNewProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadLanguages()
        binding.etFecha.setText(getCurrentDate())
        binding.etFecha.setOnClickListener { showCalendar() }
        binding.btnGuardarProyecto.setOnClickListener { saveProject() }
    }

    private fun saveProject() {
        val nombre = binding.etNombreProyecto.text.toString()
        val descripcion = binding.etDescripcion.text.toString()
        val fecha = binding.etFecha.text.toString()
        val prioridad = when  {
            binding.rbBaja.isChecked -> "Baja"
            binding.rbMedia.isChecked -> "Media"
            binding.rbAlta.isChecked -> "Alta"
            else -> ""
        }
        val tiempoEmplear = binding.etTiempoEmplear.text.toString().toIntOrNull() ?: 0
        val lenguaje = binding.spLenguajes.selectedItem.toString()
        val detalle = binding.etDetalle.text.toString()

        if (nombre.isNotEmpty() && descripcion.isNotEmpty()
            && fecha.isNotEmpty() && prioridad.isNotEmpty()
            && tiempoEmplear > 0 && lenguaje.isNotEmpty()
            && detalle.isNotEmpty()) {
            lifecycleScope.launch(Dispatchers.IO) {
                val app = applicationContext as MyApplication
                val project = Project(
                    id = 0,
                    nombre = nombre,
                    descripcion = descripcion,
                    fecha = fecha,
                    prioridad = prioridad,
                    tiempoEmplear = tiempoEmplear,
                    lenguaje = lenguaje,
                    detalle = detalle
                )
                app.room.projectDao().insertProject(project)
                val intent = Intent(this@NewProjectActivity, ProjectListActivity::class.java)
                startActivity(intent)
            }
            Toast.makeText(this@NewProjectActivity, "Proyecto guardado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@NewProjectActivity, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentDate(): String{
        val currentDate = Calendar.getInstance()
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        val month = currentDate.get(Calendar.MONTH) + 1
        val year = currentDate.get(Calendar.YEAR)
        return "$day/$month/$year"
    }

    private fun showCalendar() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val datePicker = DatePickerDialog(this, { _, year, month, day ->
            val date = "$day/${month + 1}/$year"
            binding.etFecha.setText(date)
        }, year, month, day)
        datePicker.show()
    }

    private fun loadLanguages() {

        lifecycleScope.launch{
            val languages = getLanguages()
            val languagesNames = languages.map { it.nombre }
            val adapter = ArrayAdapter(this@NewProjectActivity, android.R.layout.simple_spinner_item, languagesNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spLenguajes.adapter = adapter

        }

    }

    suspend fun getLanguages(): List<Language> {
        val app = applicationContext as MyApplication
        return app.room.languageDao().getAllLanguages()
    }
}