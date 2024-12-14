package com.example.mycodingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nombre: String,
    val tiempoEmplear: Int,
    val prioridad: String,
    val descripcion: String,
    val fecha: String,
    val lenguaje: String,
    val detalle: String
) : Serializable