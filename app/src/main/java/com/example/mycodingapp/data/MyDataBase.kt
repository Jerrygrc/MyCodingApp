package com.example.mycodingapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mycodingapp.model.Language
import com.example.mycodingapp.model.Project

@Database(entities = [Language::class, Project::class], version = 2)
abstract class MyDataBase : RoomDatabase() {
    abstract fun languageDao(): LanguageDao
    abstract fun projectDao(): ProjectDao
}