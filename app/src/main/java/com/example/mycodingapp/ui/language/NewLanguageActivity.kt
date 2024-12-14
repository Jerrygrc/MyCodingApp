package com.example.mycodingapp.ui.language

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.mycodingapp.app.MyApplication
import com.example.mycodingapp.R
import com.example.mycodingapp.databinding.ActivityNewLanguageBinding
import com.example.mycodingapp.model.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewLanguageActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityNewLanguageBinding
    private val binding: ActivityNewLanguageBinding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityNewLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnGuardarLenguaje.setOnClickListener { guardarLenguaje() }

    }

    private fun guardarLenguaje() {
        val app = applicationContext as MyApplication
        val languageName = binding.etLenguaje.text.toString()


        if (languageName.isNotEmpty()) {
            lifecycleScope.launch(Dispatchers.IO) {
                val languageBBDD = app.room.languageDao().getLanguageByName(languageName)
                if (languageBBDD == null){
                    app.room.languageDao().insertLanguage(
                        Language(
                            id = 0,
                            nombre = languageName
                        )
                    )
                }
            }
            Toast.makeText(this, "Lenguaje guardado", Toast.LENGTH_SHORT).show()
            binding.etLenguaje.text.clear()
            val intent = Intent(this, LanguageListActivity::class.java)
            startActivity(intent)
        } else{
            lifecycleScope.launch(Dispatchers.Main){
                Toast.makeText(this@NewLanguageActivity, "El campo no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }
}