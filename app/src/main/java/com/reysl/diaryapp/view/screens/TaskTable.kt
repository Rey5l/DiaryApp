package com.reysl.diaryapp.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.reysl.diaryapp.model.Task
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

@Composable
fun TaskTable(tasks: List<Task>) {
    val timeSlots = (0..23).map { hour ->
        LocalTime.of(hour, 0) to LocalTime.of((hour + 1) % 24, 0)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        items(timeSlots.chunked(4)) { rowSlots ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowSlots.forEach { (start, end) ->
                    val tasksForSlot = tasks.filter { task ->
                        val taskStartTime = Instant.ofEpochMilli(task.dateStart)
                            .atZone(ZoneId.systemDefault()).toLocalTime()
                        val taskEndTime = Instant.ofEpochMilli(task.dateFinish)
                            .atZone(ZoneId.systemDefault()).toLocalTime()
                        (taskStartTime < end && taskEndTime > start)
                    }

                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = if (tasksForSlot.isNotEmpty())
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = String.format("%02d:00 - %02d:00", start.hour, end.hour),
                                style = MaterialTheme.typography.titleSmall,
                                textAlign = TextAlign.Center,
                                color = if (tasksForSlot.isNotEmpty())
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                            if (tasksForSlot.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = tasksForSlot.joinToString(separator = "\n") { it.name },
                                    style = MaterialTheme.typography.titleSmall,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}