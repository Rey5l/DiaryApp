package com.reysl.diaryapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reysl.diaryapp.data.dao.TaskDao
import com.reysl.diaryapp.model.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    companion object {
        const val NAME = "Task_DB"
    }

    abstract fun taskDao(): TaskDao
}