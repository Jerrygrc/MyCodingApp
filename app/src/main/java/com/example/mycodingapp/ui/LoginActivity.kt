package com.example.mycodingapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.example.mycodingapp.R

import com.example.mycodingapp.databinding.ActivityLoginBinding
import com.example.mycodingapp.ui.project.ProjectListActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLoginBinding
    private val binding: ActivityLoginBinding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCrearCuentaNueva.setOnClickListener { navigateToNewAccountActivity() }

        binding.btnAcceder.setOnClickListener {
            val nombreIngresado = binding.etNameLogin.text.trim().toString()
            val contraseñaIngresada = binding.etPwdLogin.text.trim().toString()

            if (nombreIngresado.isEmpty() || contraseñaIngresada.isEmpty()) {
                mostrarToast("Por favor, completa todos los campos.")
                return@setOnClickListener
            }
            verificarDatos(nombreIngresado, contraseñaIngresada)
        }

    }

    private fun navigateToProjectListActivity() {
        val intent = Intent(this, ProjectListActivity::class.java)
        startActivity(intent)
    }

    private fun verificarDatos(nombreIngresado: String, contraseñaIngresada: String) {
        lifecycleScope.launch {
            leerDatos().collect { (nombreAlmacenado, contraseñaAlmacenada) ->
                if (nombreIngresado == nombreAlmacenado && contraseñaIngresada == contraseñaAlmacenada) {
                    mostrarToast("Bienvenid@ 🎉")
                    navigateToProjectListActivity()
                } else {
                    mostrarToast("Datos incorrectos. Regístrate 😊")
                }
            }
        }
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToNewAccountActivity() {
        val intent = Intent(this, NewAccountActivity::class.java)
        startActivity(intent)
    }

    private fun leerDatos(): Flow<Pair<String?, String?>> {
        val nombreKey = stringPreferencesKey("nombre")
        val contraseñaKey = stringPreferencesKey("contraseña")

        return this.dataStore.data.map { preferences ->
            val nombre = preferences[nombreKey]
            val contraseña = preferences[contraseñaKey]
            Pair(nombre, contraseña)
        }
    }

}
