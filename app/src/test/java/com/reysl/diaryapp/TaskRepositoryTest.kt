package com.reysl.diaryapp

import com.reysl.diaryapp.data.dao.TaskDao
import com.reysl.diaryapp.data.repository.TaskRepository
import com.reysl.diaryapp.model.Task
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import io.mockk.mockk

class TaskRepositoryTest {
    private lateinit var taskDao: TaskDao
    private lateinit var taskRepository: TaskRepository

    @Before
    fun setup() {
        taskDao = mockk()
        taskRepository = TaskRepository(taskDao)
    }

    @Test
    fun testAddTask() = runBlocking {
        val task = Task(
            name = "Test Task",
            description = "Task description",
            dateStart = System.currentTimeMillis(),
            dateFinish = System.currentTimeMillis() + 3600000
        )

        coEvery { taskDao.insertTask(task) } returns Unit

        taskRepository.addTask(task)

        coVerify { taskDao.insertTask(task) }
    }

    @Test
    fun testGetTaskForDate() = runBlocking {
        val task1 = Task(
            name = "Test Task 1",
            description = "Description 1",
            dateStart = System.currentTimeMillis(),
            dateFinish = System.currentTimeMillis() + 3600000
        )

        val task2 = Task(
            name = "Test Task 2",
            description = "Description 2",
            dateStart = System.currentTimeMillis() + 1000000,
            dateFinish = System.currentTimeMillis() + 4600000
        )

        coEvery { taskDao.getTasksForDate(any(), any()) } returns listOf(task1, task2)

        val tasks = taskRepository.getTaskForDate(System.currentTimeMillis(), System.currentTimeMillis() + 5000000)

        assertEquals(2, tasks.size)
    }

    @Test
    fun testDeleteTask() = runBlocking {
        val task = Task(
            name = "Test Task to Delete",
            description = "Description",
            dateStart = System.currentTimeMillis(),
            dateFinish = System.currentTimeMillis() + 3600000
        )

        coEvery { taskDao.deleteTask(task) } returns Unit

        taskRepository.deleteTask(task)

        coVerify { taskDao.deleteTask(task) }
    }

    @Test
    fun testGetTaskForDateWhenNoTasks() = runBlocking {
        coEvery { taskDao.getTasksForDate(any(), any()) } returns emptyList()

        val tasks = taskRepository.getTaskForDate(System.currentTimeMillis(), System.currentTimeMillis() + 5000000)

        assertTrue(tasks.isEmpty())
    }
}