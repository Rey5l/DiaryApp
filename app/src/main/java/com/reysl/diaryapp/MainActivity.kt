package com.reysl.diaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.reysl.diaryapp.ui.theme.DiaryAppTheme
import com.reysl.diaryapp.view.screens.TaskListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiaryAppTheme {
                TaskListScreen()
            }
        }
    }
}
