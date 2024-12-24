package com.reysl.diaryapp.data.repository

import com.reysl.diaryapp.data.dao.TaskDao
import com.reysl.diaryapp.model.Task

class TaskRepository(private val taskDao: TaskDao) {
    suspend fun getTaskForDate(startDate: Long, endDate: Long): List<Task> {
        return taskDao.getTasksForDate(startDate, endDate)
    }

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}