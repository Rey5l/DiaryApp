package com.reysl.diaryapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("date_start")
    val dateStart: Long,
    @ColumnInfo("date_finish")
    val dateFinish: Long,
    val name: String,
    val description: String
)
