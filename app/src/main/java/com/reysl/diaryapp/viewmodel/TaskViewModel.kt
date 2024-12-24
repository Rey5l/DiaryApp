package com.reysl.diaryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reysl.diaryapp.data.repository.TaskRepository
import com.reysl.diaryapp.model.Task
import java.time.LocalDate

class TaskViewModel : ViewModel() {
    private val repository = TaskRepository()
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks
    fun fetchTasksForDate(date: LocalDate) {
        _tasks.value = repository.getTasksForDate(date)
    }
}