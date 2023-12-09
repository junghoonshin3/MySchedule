package kr.sjh.myschedule.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val name: String, val route: String, val icon: ImageVector, val badgeCount: Int = 0
) {
    object Schedule : Screen(
        name = "schedule", route = "schedule", icon = Icons.Default.Schedule
    )

    object Settings : Screen(
        name = "settings", route = "settings", icon = Icons.Default.Settings
    )
}

