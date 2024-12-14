package com.example.mycodingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Language(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var nombre: String,
)
