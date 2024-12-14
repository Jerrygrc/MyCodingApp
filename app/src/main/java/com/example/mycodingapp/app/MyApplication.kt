package com.example.mycodingapp.app

import android.app.Application
import androidx.room.Room
import com.example.mycodingapp.data.MyDataBase

class MyApplication : Application() {
    lateinit var room: MyDataBase

    override fun onCreate() {
        super.onCreate()
        room = Room.databaseBuilder(
            applicationContext,
            MyDataBase::class.java,
            "MyDataBase"
        ).fallbackToDestructiveMigration().build()
    }
}