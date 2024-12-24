package com.reysl.diaryapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reysl.diaryapp.model.Task

@Dao
interface TaskDao {
    @Query("Select * From tasks Where date_start >= :startDate AND date_finish <= :endDate")
    suspend fun getTasksForDate(startDate: Long, endDate: Long): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}