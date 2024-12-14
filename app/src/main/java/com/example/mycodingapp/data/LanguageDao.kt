package com.example.mycodingapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mycodingapp.model.Language

@Dao
interface LanguageDao {
    @Query("SELECT * FROM language")
    suspend fun getAllLanguages(): List<Language>

    @Query("DELETE FROM language WHERE nombre = ''")
    suspend fun deleteEmptyLanguages()

    @Query("SELECT * FROM language WHERE nombre = :name LIMIT 1")
    suspend fun getLanguageByName(name: String): Language?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLanguage(language: Language)

    @Delete
    suspend fun deleteLanguage(language: Language)

    @Update
    suspend fun updateLanguage(language: Language)


}
