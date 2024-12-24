package com.reysl.diaryapp.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.reysl.diaryapp.model.Task
import com.reysl.diaryapp.ui.theme.DiaryAppTheme
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun CalendarView(selectedDate: LocalDate, tasks: List<Task>, onDateSelected: (LocalDate) -> Unit) {
    DiaryAppTheme {
        val currentMonthState = remember { mutableStateOf(YearMonth.now()) }
        val currentMonth = currentMonthState.value
        val daysInMonth = currentMonth.lengthOfMonth()
        val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    currentMonthState.value = currentMonth.minusMonths(1)
                }) {
                    Text(text = "<", style = MaterialTheme.typography.titleMedium)
                }
                Text(
                    text = currentMonth.format(DateTimeFormatter.ofPattern("MMM yyyy")),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Button(onClick = {
                    currentMonthState.value = currentMonth.plusMonths(1)
                }) {
                    Text(text = ">", style = MaterialTheme.typography.titleMedium)
                }
            }

            LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.fillMaxWidth()) {
                items(firstDayOfMonth) {
                    Spacer(modifier = Modifier.size(40.dp))
                }

                items(daysInMonth) { day ->
                    val date = currentMonth.atDay(day + 1)
                    val isSelected = date == selectedDate
                    val hasTask = tasks.any { task ->
                        val taskStartDate =
                            Instant.ofEpochMilli(task.dateStart).atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        val taskEndDate =
                            Instant.ofEpochMilli(task.dateFinish).atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        date in taskStartDate..taskEndDate
                    }

                    TextButton(
                        onClick = { onDateSelected(date) },
                        modifier = Modifier.size(40.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = when {
                                isSelected -> MaterialTheme.colorScheme.primary
                                hasTask -> MaterialTheme.colorScheme.secondary
                                else -> MaterialTheme.colorScheme.surface
                            }
                        )
                    ) {
                        Text(
                            text = (day + 1).toString(),
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    }
}