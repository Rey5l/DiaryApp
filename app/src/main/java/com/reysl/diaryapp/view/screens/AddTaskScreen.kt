package com.reysl.diaryapp.view.screens

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reysl.diaryapp.R
import com.reysl.diaryapp.model.Task
import com.reysl.diaryapp.ui.theme.DiaryAppTheme
import com.reysl.diaryapp.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AddTaskScreen(
    viewModel: TaskViewModel = viewModel()
) {
    DiaryAppTheme {
        var taskName by remember { mutableStateOf("") }
        var taskDescription by remember { mutableStateOf("") }
        var dateStart by remember { mutableStateOf(LocalDate.now()) }
        var startHour by remember { mutableStateOf("08:00") }
        var endHour by remember { mutableStateOf("09:00") }

        val taskAddResult = viewModel.taskAddResult

        val context = LocalContext.current

        LaunchedEffect(Unit) {
            taskAddResult.collect { result ->
                if (result.isSuccess) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.add_task_success), Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error, result.exceptionOrNull()?.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        fun showDatePickerDialog() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    dateStart = LocalDate.of(
                        selectedYear,
                        selectedMonth + 1,
                        selectedDay
                    )
                },
                year,
                month,
                day
            ).show()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = {
                    Text(
                        stringResource(R.string.task_name),
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            TextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = {
                    Text(
                        stringResource(R.string.description),
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        R.string.date,
                        dateStart.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                    ),
                    style = MaterialTheme.typography.titleMedium,
                )
                Button(onClick = { showDatePickerDialog() }) {
                    Text(stringResource(R.string.select_date), style = MaterialTheme.typography.titleSmall)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = startHour,
                    onValueChange = { startHour = it },
                    label = { Text(stringResource(R.string.start_time), style = MaterialTheme.typography.titleSmall) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = endHour,
                    onValueChange = { endHour = it },
                    label = { Text(stringResource(R.string.end_time), style = MaterialTheme.typography.titleSmall) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    try {
                        val (startHourPart, startMinutePart) = startHour.split(":")
                            .map { it.toInt() }
                        val (endHourPart, endMinutePart) = endHour.split(":").map { it.toInt() }

                        if (startHourPart !in 0..23 || endHourPart !in 0..23 ||
                            startMinutePart !in 0..59 || endMinutePart !in 0..59
                        ) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.invalid_time_format), Toast.LENGTH_SHORT
                            )
                                .show()
                            return@Button
                        }

                        val zoneId = ZoneId.systemDefault()

                        val startMillis = dateStart
                            .atTime(startHourPart, startMinutePart)
                            .atZone(zoneId)
                            .toEpochSecond() * 1000

                        val endMillis = dateStart
                            .atTime(endHourPart, endMinutePart)
                            .atZone(zoneId)
                            .toEpochSecond() * 1000

                        if (endMillis <= startMillis) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.time_error_toast),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            return@Button
                        }

                        val task = Task(
                            dateStart = startMillis,
                            dateFinish = endMillis,
                            name = taskName,
                            description = taskDescription
                        )
                        viewModel.addTask(task)
                    } catch (e: Exception) {
                        val error = e.printStackTrace()
                        Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_task), style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}