package com.example.mycodingapp.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.mycodingapp.R
import com.example.mycodingapp.databinding.ActivityNewAccountBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "MY_DATA_STORE")

class NewAccountActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityNewAccountBinding
    private val binding: ActivityNewAccountBinding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityNewAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCrearCuenta.setOnClickListener {
            val nombre = binding.etNombreNuevaCuenta.text.trim().toString()
            val contraseña = binding.etPwdNuevaCuenta.text.trim().toString()
            val confirmarContraseña = binding.etPwdRepNuevaCuenta.text.toString()

            if (nombre.isNotEmpty() && contraseña.isNotEmpty() && confirmarContraseña.isNotEmpty() && contraseña == confirmarContraseña) {
                lifecycleScope.launch(Dispatchers.IO) {
                    guardarDataStore(nombre, contraseña)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@NewAccountActivity, "Cuenta creada", Toast.LENGTH_SHORT)
                            .show()
                        navigateToLoginActivity()
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Por favor, completa todos los campos correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun guardarDataStore(
        nombre: String,
        contraseña: String
    ) {
        dataStore.edit { editor ->
            editor[stringPreferencesKey("nombre")] = nombre
            editor[stringPreferencesKey("contraseña")] = contraseña
        }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}