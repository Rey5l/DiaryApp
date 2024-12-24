package com.reysl.diaryapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.reysl.diaryapp.data.repository.TaskRepository
import com.reysl.diaryapp.model.Task
import com.reysl.diaryapp.viewmodel.TaskViewModel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

class TaskViewModelTest {
    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: TaskRepository
    private lateinit var viewModel: TaskViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        repository = mock()
        viewModel = TaskViewModel()
        viewModel.setRepository(repository)
    }

    @Test
    fun `fetchTasksForDate should handle empty result`() = runTest {
        val testDate = LocalDate.now()
        val emptyList = emptyList<Task>()
        whenever(repository.getTaskForDate(any(), any())).thenReturn(emptyList)

        viewModel.fetchTasksForDate(testDate)

        Assert.assertEquals(emptyList, viewModel.tasks.value)
    }

    @Test
    fun `fetchTasksForDate should handle exception`() = runTest {
        val testDate = LocalDate.now()
        whenever(repository.getTaskForDate(any(), any())).thenThrow(RuntimeException("DB Error"))

        viewModel.fetchTasksForDate(testDate)

        Assert.assertEquals(emptyList<Task>(), viewModel.tasks.value)
    }

    @Test
    fun `addTask should call repository addTask`() = runTest(testDispatcher) {
        val testTask = Task(1, 1734739200000, 1734742800000, "Add Task", "Description")
        viewModel.addTask(testTask)

        verify(repository).addTask(testTask)
    }

    @Test
    fun `deleteTask should handle exception`() = runTest {
        val testTask = Task(1, 1734739200000, 1734742800000, "Delete Task", "Description")
        whenever(repository.deleteTask(testTask)).thenThrow(RuntimeException("DB Error"))

        viewModel.deleteTask(testTask)

        verify(repository).deleteTask(testTask)
    }
}