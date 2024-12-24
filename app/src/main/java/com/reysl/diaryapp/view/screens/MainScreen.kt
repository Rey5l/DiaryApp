package com.reysl.diaryapp.view.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.reysl.diaryapp.ui.theme.DiaryAppTheme
import com.reysl.diaryapp.view.navigation.NavItem

@Composable
fun MainScreen() {
    DiaryAppTheme {
        val navItemList = listOf(
            NavItem.Tasks,
            NavItem.Adding
        )

        var selectedIndex by remember {
            mutableIntStateOf(0)
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar {
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            icon = { Icon(imageVector = navItem.icon, contentDescription = "Icon") },
                            label = { Text(text = navItem.label, style = MaterialTheme.typography.titleSmall) },
                        )
                    }
                }
            }
        ) { innerPadding ->
            ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
        }
    }
}

@Composable
fun ContentScreen(modifier: Modifier, selectedIndex: Int) {
    when (selectedIndex) {
        0 -> TaskListScreen(modifier)
        1 -> AddTaskScreen()
    }
}
