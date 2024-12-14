package com.example.mycodingapp.ui.project

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
import com.example.mycodingapp.databinding.ActivityProjectListBinding
import com.example.mycodingapp.model.Language
import com.example.mycodingapp.model.Project
import com.example.mycodingapp.ui.language.LanguageListActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectListActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityProjectListBinding
    private val binding: ActivityProjectListBinding get() = _binding
    private lateinit var adapter: ProjectRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityProjectListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.fabCrearProyecto.setOnClickListener {
            checkLanguagesAndNavigate()
        }

        binding.btnAddLenguaje.setOnClickListener {
            navigateToLanguageListActivity()

        }

        binding.rvProjects.layoutManager = LinearLayoutManager(this)
        loadProjects()

    }

    private fun loadProjects() {
        lifecycleScope.launch(Dispatchers.IO) {
            val app = applicationContext as MyApplication
            val projects = app.room.projectDao().getAllProjects()
            withContext(Dispatchers.Main) {
                adapter = ProjectRecyclerViewAdapter(
                    projects = projects as MutableList<Project>,
                    onDeleteClick = { project -> deleteProject(project, projects) },
                    onProjectClick = { project -> navigateToProjectDetailActivity(project) } // Nuevo callback
                )
                binding.rvProjects.adapter = adapter
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteProject(project: Project, projects: MutableList<Project>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar proyecto")
        builder.setMessage("¿Estás seguro de que deseas eliminar este proyecto?")
        builder.setPositiveButton("Sí") { _, _ ->
            lifecycleScope.launch(Dispatchers.IO) {

                val app = applicationContext as MyApplication
                app.room.projectDao().deleteProject(project)
                withContext(Dispatchers.Main) {
                    projects.remove(project)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this@ProjectListActivity, "Proyecto eliminado", Toast.LENGTH_SHORT).show()
                }
            }
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun navigateToProjectDetailActivity(project: Project) {
        val intent = Intent(this, ProjectDescriptionActivity::class.java)
        intent.putExtra("project", project)
        startActivity(intent)
    }

    private fun navigateToLanguageListActivity() {
        val intent = Intent(this, LanguageListActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToNewProjectActivity() {
        val intent = Intent(this, NewProjectActivity::class.java)
        startActivity(intent)
    }

    private fun checkLanguagesAndNavigate() {
        lifecycleScope.launch(Dispatchers.IO){
            val languages = getLanguagesFromDatabase()

            withContext(Dispatchers.Main){
                if (languages.isEmpty()){
                    Toast.makeText(this@ProjectListActivity, "No hay lenguajes creados, crea uno", Toast.LENGTH_SHORT).show()
                    navigateToLanguageListActivity()
                }else{
                    navigateToNewProjectActivity()
                }
            }
        }
    }

    private suspend fun getLanguagesFromDatabase(): List<Language> {
        val app = applicationContext as MyApplication
        return app.room.languageDao().getAllLanguages()
    }

}