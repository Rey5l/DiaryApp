package com.reysl.diaryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reysl.diaryapp.core.MainApplication
import com.reysl.diaryapp.data.repository.TaskRepository
import com.reysl.diaryapp.model.Task
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset

class TaskViewModel : ViewModel() {
    private var repository = TaskRepository(taskDao = MainApplication.taskDatabase.taskDao())
    private val _taskAddResult = MutableSharedFlow<Result<Unit>>()
    val taskAddResult: SharedFlow<Result<Unit>> get() = _taskAddResult.asSharedFlow()
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    fun setRepository(repository: TaskRepository) {
        this.repository = repository
    }

    fun fetchTasksForDate(date: LocalDate) {
        viewModelScope.launch {
            val startOfDay = date.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000
            val endOfDay = date.atStartOfDay().plusDays(1).toEpochSecond(ZoneOffset.UTC) * 1000
            _tasks.value = repository.getTaskForDate(startOfDay, endOfDay)
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.addTask(task)
                fetchTasksForDate(LocalDate.now())
                _taskAddResult.emit(Result.success(Unit))
            } catch(e: Exception) {
                _taskAddResult.emit(Result.failure(e))
            }

        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            fetchTasksForDate(LocalDate.now())
        }
    }
}