package com.reysl.diaryapp.core

import android.app.Application
import androidx.room.Room
import com.reysl.diaryapp.data.db.TaskDatabase

class MainApplication : Application() {
    companion object {
        lateinit var taskDatabase: TaskDatabase
    }

    override fun onCreate() {
        super.onCreate()
        taskDatabase = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            TaskDatabase.NAME
        ).build()
    }
}