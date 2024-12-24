package com.reysl.diaryapp.data.repository

import com.reysl.diaryapp.model.Task
import java.time.LocalDate
import java.time.ZoneOffset

class TaskRepository {
    private val tasks = listOf(
        Task(
            1,
            LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000,
            LocalDate.now().atStartOfDay().plusHours(1).toEpochSecond(ZoneOffset.UTC) * 1000,
            "Task 1",
            "Description 1"
        ),
        Task(
            2,
            LocalDate.now().plusDays(1).atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000,
            LocalDate.now().plusDays(1).atStartOfDay().plusHours(2)
                .toEpochSecond(ZoneOffset.UTC) * 1000,
            "Task 2",
            "Description 2"
        ),
        Task(
            3,
            LocalDate.now().minusDays(1).atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000,
            LocalDate.now().minusDays(1).atStartOfDay().plusHours(3)
                .toEpochSecond(ZoneOffset.UTC) * 1000,
            "Task 3",
            "Description 3"
        )
    )
    fun getTasksForDate(date: LocalDate): List<Task> {
        val startOfDay = date.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000
        val endOfDay = date.plusDays(1).atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000
        return tasks.filter { it.dateStart in startOfDay until endOfDay }
    }
}