package com.reysl.diaryapp.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(val label: String, val icon: ImageVector) {
    data object Tasks: NavItem("Tasks", Icons.Default.DateRange)
    data object Adding: NavItem("Adding", Icons.Default.Add)
}