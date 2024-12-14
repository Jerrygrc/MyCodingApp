package com.example.mycodingapp.ui.language

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycodingapp.R
import com.example.mycodingapp.app.MyApplication
import com.example.mycodingapp.databinding.ActivityLanguageListBinding
import com.example.mycodingapp.model.Language
import com.example.mycodingapp.ui.project.ProjectListActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LanguageListActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLanguageListBinding
    private val binding: ActivityLanguageListBinding get() = _binding
    private lateinit var adapter: LanguageRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityLanguageListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        readLanguages()
        binding.fabAddLenguaje.setOnClickListener {
            navigateToNewLanguageActivity()
        }

        binding.btnProyectos.setOnClickListener {
            navigateToProjectListActivity()
        }

    }

    private fun navigateToProjectListActivity() {
        val intent = Intent(this, ProjectListActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToNewLanguageActivity() {
        val intent = Intent(this, NewLanguageActivity::class.java)
        startActivity(intent)
    }

    private fun readLanguages(): MutableList<Language> {
        val app = applicationContext as MyApplication
        val languages = mutableListOf<Language>()

        lifecycleScope.launch(Dispatchers.IO) {
            app.room.languageDao().deleteEmptyLanguages()
            languages.addAll(app.room.languageDao().getAllLanguages())
            withContext(Dispatchers.Main) {
                setupRecyclerView(languages)
            }
        }
        return languages
    }

    private fun setupRecyclerView(languages: MutableList<Language>) {
        adapter = LanguageRecyclerViewAdapter(
            languages,
            onDeleteClickListener = {  language -> deleteLanguage(language) },
            onEditLongClickListener = { language -> editLanguage(language) }
        )
        binding.rvLanguajes.adapter = adapter
        binding.rvLanguajes.layoutManager = LinearLayoutManager(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun editLanguage(language: Language) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar lenguaje")
        val input = android.widget.EditText(this)
        input.setText(language.nombre)
        builder.setView(input)
        builder.setPositiveButton("Guardar") { _, _ ->
            val newName = input.text.toString()
            if (newName.isNotEmpty()) {
                val app = applicationContext as MyApplication
                lifecycleScope.launch(Dispatchers.IO) {
                    language.nombre = newName
                    app.room.languageDao().updateLanguage(language)
                    withContext(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()
                        Toast.makeText(
                            this@LanguageListActivity,
                            "Lenguaje actualizado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                }
            }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteLanguage(language: Language) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar lenguaje")
        builder.setMessage("¿Estás seguro de que deseas eliminar este lenguaje?")
        builder.setPositiveButton("Sí") { _, _ ->
            val app = applicationContext as MyApplication
            lifecycleScope.launch(Dispatchers.IO) {
                app.room.languageDao().deleteLanguage(language)
                withContext(Dispatchers.Main) {
                    adapter.removeLanguage(language)
                    Toast.makeText(this@LanguageListActivity, "Lenguaje eliminado", Toast.LENGTH_SHORT).show()
                }
            }
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}