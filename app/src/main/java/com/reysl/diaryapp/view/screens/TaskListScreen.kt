package com.reysl.diaryapp.view.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reysl.diaryapp.R
import com.reysl.diaryapp.model.Task
import com.reysl.diaryapp.ui.theme.DiaryAppTheme
import com.reysl.diaryapp.view.components.CalendarView
import com.reysl.diaryapp.view.components.TaskTable
import com.reysl.diaryapp.viewmodel.TaskViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = viewModel()
) {
    DiaryAppTheme {
        val tasks by viewModel.tasks.observeAsState(emptyList())
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }
        var showTasks by remember { mutableStateOf(false) }

        LaunchedEffect(tasks) { viewModel.fetchTasksForDate(selectedDate) }

        Column(
            modifier = modifier
        ) {
            CalendarView(selectedDate = selectedDate, tasks = tasks) { date ->
                selectedDate = date
                viewModel.fetchTasksForDate(date)
            }
            Text(
                stringResource(
                    R.string.selected_date,
                    selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                ),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = { showTasks = !showTasks },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(if (showTasks) stringResource(R.string.hide_tasks) else stringResource(R.string.show_tasks), style = MaterialTheme.typography.titleSmall)
            }

            if (showTasks) {
                LazyColumn {
                    items(tasks) { task ->
                        TaskItem(task, viewModel)
                    }
                }
            }

            TaskTable(tasks)
        }
    }
}

@Composable
fun TaskItem(task: Task, viewModel: TaskViewModel) {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val startDatetime = Instant.ofEpochMilli(task.dateStart)
        .atZone(ZoneId.systemDefault())
        .format(dateTimeFormatter)

    val endDateTime = Instant.ofEpochMilli(task.dateFinish)
        .atZone(ZoneId.systemDefault())
        .format(dateTimeFormatter)


    Card(modifier = Modifier.padding(8.dp)) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(onClick = {

            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Text(text = task.name, style = MaterialTheme.typography.titleMedium)
            Text(text = stringResource(R.string.time, startDatetime, endDateTime), style = MaterialTheme.typography.titleSmall)
            Text(text = task.description, style = MaterialTheme.typography.titleSmall)
        }
    }
}